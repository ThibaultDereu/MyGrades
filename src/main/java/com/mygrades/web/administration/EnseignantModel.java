package com.mygrades.web.administration;

import java.util.ArrayList;
import java.util.List;

public class EnseignantModel {

	private Long id;
	private Long idUtilisateur;
	private String prenom;
	private String nom;
	
	private List<Long> idModules = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdUtilisateur() {
		return idUtilisateur;
	}
	public void setIdUtilisateur(Long idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public List<Long> getModules() {
		return idModules;
	}
	public void setModules(List<Long> idModules) {
		this.idModules = idModules;
	}
	
}
