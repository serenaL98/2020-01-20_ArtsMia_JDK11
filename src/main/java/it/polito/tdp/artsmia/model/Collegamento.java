package it.polito.tdp.artsmia.model;

public class Collegamento implements Comparable<Collegamento>{
	
	private Integer cod1;
	private String nom1;
	private Integer cod2;
	private String nom2;
	private Integer peso;
	
	
	public Collegamento(Integer cod1, String nom1, Integer cod2, String nom2, Integer peso) {
		super();
		this.cod1 = cod1;
		this.nom1 = nom1;
		this.cod2 = cod2;
		this.nom2 = nom2;
		this.peso = peso;
	}
	
	
	public Integer getCod1() {
		return cod1;
	}
	public void setCod1(Integer cod1) {
		this.cod1 = cod1;
	}
	public String getNom1() {
		return nom1;
	}
	public void setNom1(String nom1) {
		this.nom1 = nom1;
	}
	public Integer getCod2() {
		return cod2;
	}
	public void setCod2(Integer cod2) {
		this.cod2 = cod2;
	}
	public String getNom2() {
		return nom2;
	}
	public void setNom2(String nom2) {
		this.nom2 = nom2;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}


	@Override
	public int compareTo(Collegamento o) {
		return -this.peso.compareTo(o.getPeso());
	}


	@Override
	public String toString() {
		if(this.peso == 1)
			return nom1+" --- "+nom2+", "+peso+" esposizione\n";
		else
			return nom1+" --- "+nom2+", "+peso+" esposizioni\n";
	}
	
	

}
