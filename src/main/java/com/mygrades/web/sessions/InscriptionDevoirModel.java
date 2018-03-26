package com.mygrades.web.sessions;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

public class InscriptionDevoirModel {

	private Long id;
	private String nomDevoir;
	private Double note;
	private Integer coefficient;
	private String numeroEtudiant;
	private String prenomEtudiant;
	private String nomEtudiant;
	
	
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
	public Double getNote() {
		return note;
	}
	public void setNote(Double note) {
		this.note = note;
	}
	public Integer getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(Integer coefficient) {
		this.coefficient = coefficient;
	}
	public String getNumeroEtudiant() {
		return numeroEtudiant;
	}
	public void setNumeroEtudiant(String numeroEtudiant) {
		this.numeroEtudiant = numeroEtudiant;
	}
	public String getPrenomEtudiant() {
		return prenomEtudiant;
	}
	public void setPrenomEtudiant(String prenomEtudiant) {
		this.prenomEtudiant = prenomEtudiant;
	}
	public String getNomEtudiant() {
		return nomEtudiant;
	}
	public void setNomEtudiant(String nomEtudiant) {
		this.nomEtudiant = nomEtudiant;
	}
	
}
