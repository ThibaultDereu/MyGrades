package com.mygrades.web.administration;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class UtilisateurModel {

	private Long id;
	
	@NotBlank(message = "Veuillez saisir une adresse e-mail.")
	@Email(message = "Veuillez saisir une adresse e-mail valide.")
	private String email;
	
	private String password;
	
	@NotBlank(message = "Le prénom doit être renseigné.")
	private String prenom;
	
	@NotBlank(message = "Le nom doit être renseigné.")
	private String nom;
	
	private Boolean admin = false;
	
	private Long idEtudiant;
	
	private Long idEnseignant;

	/**
	 * constructeur par défaut pour initialisation de formulaire.
	 */
	public UtilisateurModel() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Long getIdEtudiant() {
		return idEtudiant;
	}

	public void setIdEtudiant(Long idEtudiant) {
		this.idEtudiant = idEtudiant;
	}

	public Long getIdEnseignant() {
		return idEnseignant;
	}

	public void setIdEnseignant(Long idEnseignant) {
		this.idEnseignant = idEnseignant;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
