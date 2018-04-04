package com.mygrades.web.mesNotes;

public class InscriptionDevoirModel {

	private Long id;
	private String nomDevoir;
	private Integer coefficient;
	private CalculateurModel calculateur;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomDevoir() {
		return nomDevoir;
	}

	public void setNomDevoir(String nomDevoir) {
		this.nomDevoir = nomDevoir;
	}

	public Integer getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Integer coefficient) {
		this.coefficient = coefficient;
	}

	public CalculateurModel getCalculateur() {
		return calculateur;
	}

	public void setCalculateur(CalculateurModel calculateur) {
		this.calculateur = calculateur;
	}

}
