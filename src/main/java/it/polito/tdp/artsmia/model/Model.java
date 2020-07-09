package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private List<String> ruoli;
	
	//grafo semplice, pesato, non orientato
	private Graph<String, DefaultWeightedEdge> grafo;
	private Map<Integer, String> mappaArtisti;
	private List<String> artisti;
	private List<Collegamento> collegamenti;
	
	public Model() {
		this.dao = new ArtsmiaDAO();
		this.ruoli = new ArrayList<>();
		//this.mappaArtisti = new HashMap<>();
		//this.artisti = new ArrayList<>();
		this.collegamenti = new ArrayList<>();
	}

	public List<String> elencoRuoli(){
		this.ruoli = this.dao.elencoRuoli();
		return this.ruoli;
	}
	
	public void creaGrafo(String ruolo) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.mappaArtisti = new HashMap<>();
		this.artisti = new ArrayList<>();
		
		this.mappaArtisti = this.dao.prendiArtisti(ruolo);
		
		for(String s: this.mappaArtisti.values()) {
			this.artisti.add(s);
		}
		
		//VERTICI
		Graphs.addAllVertices(this.grafo, this.artisti);
		
		this.collegamenti = this.dao.prendiCollegamento(mappaArtisti, ruolo);
		
		//ARCHI
		for(Collegamento c: this.collegamenti) {
			//if(this.grafo.containsVertex(c.getNom1()) && this.grafo.containsVertex(c.getNom2())) {
			if(this.grafo.getEdge(c.getNom1(), c.getNom2())== null) {
				Graphs.addEdge(this.grafo, c.getNom1(), c.getNom2(), c.getPeso());
				//Graphs.addEdgeWithVertices(this.grafo, c.getNom1(), c.getNom2(), c.getPeso());
			}
		}
		
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public String artistiConnessi() {
		
		List<Collegamento> ordine = new ArrayList<>();
		
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			 ordine.add(new Collegamento(0,this.grafo.getEdgeSource(e),0,this.grafo.getEdgeTarget(e), (int) this.grafo.getEdgeWeight(e)));
		}
		
		Collections.sort(ordine);
		
		String stampa = "";
		for(Collegamento c: ordine) {
			stampa += c.toString();
		}
		return stampa;
	}
	
	//---PUNTO 2---
	private List<String> soluzione;
	private int max;
	private int peso;
	
	public String risultato(int scelto) {
		
		this.soluzione = new ArrayList<>();
		this.max = 0;
		this.peso = -1;
		
		String nomeScelto = this.cercaArtista(scelto);
		
		List<String> parziale = new ArrayList<>();
		
		parziale.add(nomeScelto);
		
		ricorsione(parziale, 0);
		
		if(!this.grafo.containsVertex(nomeScelto))
			return "Non esiste l'identificativo nel grafo.";
		
		String stampa = "";
		for(String c: this.soluzione) {
			stampa += c+" --> "+peso+"\n";
		}
		return stampa;
		
	}
	
	private void ricorsione(List<String> parziale, int livello) {
		//caso finale
		if(parziale.size()>this.max) {
			this.max = parziale.size();
			this.soluzione = new ArrayList<>(parziale);
		}
		
		//caso intermedio
		//ultimo inserito
		String ultimo = parziale.get(parziale.size()-1);
		
		List<String> vicini = new ArrayList<>(Graphs.neighborListOf(this.grafo, ultimo));
		
		for(String s: vicini) {
			//primo caso
			if(!parziale.contains(s) && this.peso==-1) {
				this.peso = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, s));
				
				parziale.add(s);
				ricorsione(parziale, livello+1);
				parziale.remove(parziale.size()-1);
				
			}else if(!parziale.contains(s) && this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, s))== this.peso) {
				parziale.add(s);
				ricorsione(parziale, livello+1);
				parziale.remove(parziale.size()-1);
			}
			
		}
	}
	
	public String cercaArtista(int scelto) {
		
		for(int i = 0; i<this.mappaArtisti.size(); i++) {
			if(i == scelto) {
				return this.mappaArtisti.get(i);
			}
		}
		return null;
	}
	
}
