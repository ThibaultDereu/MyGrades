package com.mygrades.web.sessions;


public class InscriptionSessionModel {
	
	private Long id;
	private String numeroEtudiant;
	private String prenomEtudiant;
	private String nomEtudiant;
	private Long nbModules;
	private String statut;
	private Double note;
	
	public InscriptionSessionModel() {}
	

	public Long getNbModules() {
		return nbModules;
	}
	public void setNbModules(Long nbModules) {
		this.nbModules = nbModules;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public Double getNote() {
		return note;
	}
	public void setNote(Double note) {
		this.note = note;
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
	
	
}
