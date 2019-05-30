package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}
			conn.close();
			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}
			conn.close();
			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	/*
	 * Lista di tutti gli autori
	 */
	public List<Author> getAutori() {

		final String sql = "SELECT * FROM author";
		List<Author> autori = new ArrayList<Author>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				autori.add(autore);
			}
			conn.close();
			return autori;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	/*
	 * Restituisce i coautori dell'autore passato
	 */
	public List<Author> getCoautori(Author a) {

		final String sql = "SELECT DISTINCT a2.id, a2.lastname, a2.firstname\n" + 
				"FROM creator c1, creator c2, author a2\n" + 
				"WHERE c1.eprintid = c2.eprintid\n" + 
				"AND c2.authorid = a2.id\n" + 
				"AND c1.authorid = ?\n" + 
				"AND a2.id != c1.authorid\n" + 
				"ORDER BY a2.lastname ASC, a2.firstname ASC";
		List<Author> coautori = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a.getId());
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Author coautore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				coautori.add(coautore);
			}
			conn.close();
			return coautori;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public Paper getArticoloComune(Author source, Author target) {
		
		final String sql = "SELECT p.eprintid, p.title, p.issn, p.publication, p.type, p.types\n" + 
				"FROM paper p, creator c1, creator c2\n" + 
				"WHERE p.eprintid = c1.eprintid\n" + 
				"AND p.eprintid = c2.eprintid\n" + 
				"AND c1.authorid = ?\n" + 
				"AND c2.authorid = ?\n" + 
				"LIMIT 1";
		Paper paper = null;

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, source.getId());
			st.setInt(2, target.getId());

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				paper = new Paper(rs.getInt("eprintid"),rs.getString("title"), rs.getString("issn"), rs.getString("publication"), rs.getString("type"), rs.getString("types"));
			}
			conn.close();
			return paper;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
}