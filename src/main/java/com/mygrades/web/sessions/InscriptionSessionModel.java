package com.mygrades.web.sessions;


public class InscriptionSessionModel {
	
	private Long id;
	private String numeroEtudiant;
	private String prenomEtudiant;
	private String nomEtudiant;
	private Long nbModules;
	private Boolean acquis;
	private Boolean termine;
	private Double note;
	
	public InscriptionSessionModel() {}
	

	public Long getNbModules() {
		return nbModules;
	}
	public void setNbModules(Long nbModules) {
		this.nbModules = nbModules;
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


	public Boolean getAcquis() {
		return acquis;
	}


	public void setAcquis(Boolean acquis) {
		this.acquis = acquis;
	}


	public Boolean getTermine() {
		return termine;
	}


	public void setTermine(Boolean termine) {
		this.termine = termine;
	}
	
	
}
