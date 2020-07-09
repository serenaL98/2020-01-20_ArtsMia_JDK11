package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Collegamento;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> elencoRuoli() {
		
		String sql = "SELECT DISTINCT a.role ruolo " + 
				"FROM authorship a " + 
				"ORDER BY a.role ";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				String ruolo = res.getString("ruolo");
				result.add(ruolo);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione db! Controlla sintassi.");
			return null;
		}
	}
	
	public Map<Integer, String> prendiArtisti(String ruolo) {
		
		String sql = "SELECT DISTINCT t.name nom, t.artist_id cod " + 
				"FROM authorship a, artists t " + 
				"WHERE a.role = ? " + 
				"	AND a.artist_id = t.artist_id " + 
				"ORDER BY t.name ";
		Map<Integer, String> result = new HashMap<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				int cod = res.getInt("cod");
				String nome = res.getString("nom");
				
				result.put(cod, nome);
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione db! Controlla sintassi.");
			return null;
		}
	}
	
	public List<Collegamento> prendiCollegamento(Map<Integer, String> mappa, String ruolo) {
		
		String sql = "SELECT a.artist_id cod1, a1.artist_id cod2, COUNT(DISTINCT e1.exhibition_id) peso " + 
				"FROM authorship a, authorship a1, exhibition_objects e1, exhibition_objects e2 " + 
				"WHERE a.artist_id< a1.artist_id AND a1.role = a.role AND a.role = ? " + 
				"	AND a.object_id = e1.object_id " + 
				"	AND a1.object_id = e2.object_id " + 
				"	AND e1.exhibition_id = e2.exhibition_id " + 
				"GROUP BY a.artist_id, a1.artist_id " + 
				"ORDER BY a.artist_id, a1.artist_id ";
		List<Collegamento> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				if(mappa.containsKey(res.getInt("cod1")) && mappa.containsKey(res.getInt("cod2"))) {
					int cod1 = res.getInt("cod1");
					String n1 = mappa.get(res.getInt("cod1"));
					int cod2 = res.getInt("cod2");
					String n2 = mappa.get(res.getInt("cod2"));
					int peso =res.getInt("peso");
					
					Collegamento temp = new Collegamento(cod1, n1, cod2, n2, peso);
					
					result.add(temp);
				}
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione db! Controlla sintassi.");
			return null;
		}
	}
}
