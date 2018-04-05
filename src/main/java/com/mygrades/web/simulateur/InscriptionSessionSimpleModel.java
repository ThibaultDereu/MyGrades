package com.mygrades.web.simulateur;

public class InscriptionSessionSimpleModel {

	private Long id;
	private String numeroEtudiant;
	private String prenomEtudiant;
	private String nomEtudiant;
	private String nomSemestre;
	private String nomSession;
	private String nomFiliere;

	public InscriptionSessionSimpleModel(Long id, String numeroEtudiant, String prenomEtudiant, String nomEtudiant,
			String nomSemestre, String nomSession, String nomFiliere) {
		this.id = id;
		this.numeroEtudiant = numeroEtudiant;
		this.prenomEtudiant = prenomEtudiant;
		this.nomEtudiant = nomEtudiant;
		this.nomSemestre = nomSemestre;
		this.nomSession = nomSession;
		this.nomFiliere = nomFiliere;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getNomSemestre() {
		return nomSemestre;
	}

	public void setNomSemestre(String nomSemestre) {
		this.nomSemestre = nomSemestre;
	}

	public String getNomSession() {
		return nomSession;
	}

	public void setNomSession(String nomSession) {
		this.nomSession = nomSession;
	}

	public String getNomFiliere() {
		return nomFiliere;
	}

	public void setNomFiliere(String nomFiliere) {
		this.nomFiliere = nomFiliere;
	}

}
