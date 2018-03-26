package com.mygrades.web.sessions;

public class FiliereSessionsModel {
	
	private Long id;
	private String nom;
	private Long nbSessions;
	
	public FiliereSessionsModel(Long id, String nom, Long nbSessions) {
		this.id = id;
		this.nom = nom;
		this.nbSessions = nbSessions;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Long getNbSessions() {
		return nbSessions;
	}
	public void setNbSessions(Long nbSessions) {
		this.nbSessions = nbSessions;
	}
	
}
