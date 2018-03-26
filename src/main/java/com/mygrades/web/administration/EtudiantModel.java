package com.mygrades.web.administration;

import javax.validation.constraints.Pattern;


public class EtudiantModel {
	
	private Long id;
	private Long idUtilisateur;
	private String prenom;
	private String nom;
	

	@Pattern(regexp="\\d+", message="Le numéro doit être composé de chiffres uniquement.")
	private String numero;
	
	
	public EtudiantModel() {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
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
}
