package it.polito.tdp.porto.db;

import java.util.List;

import it.polito.tdp.porto.model.Author;

public class TestPortoDAO {
	
	public static void main(String args[]) {
		PortoDAO pd = new PortoDAO();
		System.out.println(pd.getAutore(85));
		List<Author> autori = pd.getAutori();
		System.out.println("Coautori di "+autori.get(2)+" : "+pd.getCoautori(autori.get(2)));
		System.out.println(pd.getArticolo(2293546));
		System.out.println(pd.getArticolo(1941144));

	}

}
