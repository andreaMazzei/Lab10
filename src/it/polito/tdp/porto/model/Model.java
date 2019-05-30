package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private List<Author> autori;
	private PortoDAO dao;
	private Graph<Author, DefaultEdge> graph;
	
	public Model() {
		dao = new PortoDAO();
		autori = this.getAutori();
	}
	
	public void createGraph() {
		graph = new SimpleGraph<Author, DefaultEdge>(DefaultEdge.class);
		
		// vertici
		Graphs.addAllVertices(graph, autori);
		
		// archi
		for(Author a : autori) {
			List<Author> coautori = dao.getCoautori(a);
			for(Author a2 : coautori) {
				if(this.graph.containsVertex(a) && this.graph.containsVertex(a2))
					graph.addEdge(a, a2);
			}
		}
		
		System.out.format("Inseriti %d vertici\n", graph.vertexSet().size() );
		System.out.format("Inseriti %d archi\n", graph.edgeSet().size() );
	}

	public List<Author> getAutori() {
		return dao.getAutori();
	}

	public List<Author> trovaCoautori(Author a) {
		this.createGraph();
		return Graphs.neighborListOf(graph, a);
	}

	public List<Paper> trovaSequenzaArticoli(Author a1, Author a2) {
		List<Paper> sequenza = new LinkedList<Paper>();
		
		// Devo trovare un cammino minimo tra a1 e a2
		// uso DIJKSTRA
		
		ShortestPathAlgorithm<Author, DefaultEdge> dijkstra = new DijkstraShortestPath<Author, DefaultEdge>(this.graph);
		
		GraphPath<Author, DefaultEdge> gp = dijkstra.getPath(a1, a2);//questo è il cammino minimo
		
		// Estraggo gli archi del cammino. Ogni vertice corrisponderà ad un paper
		List<DefaultEdge> edges = gp.getEdgeList();
		
		for(DefaultEdge e : edges) {
			Author source = graph.getEdgeSource(e);
			Author target = graph.getEdgeTarget(e);
			
			Paper p = dao.getArticoloComune(source, target);
			if(p == null)
				throw new RuntimeException("Nessun articolo in comune!");
			else
				sequenza.add(p);
		}
		return sequenza;
	}

}
