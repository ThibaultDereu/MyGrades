package com.mygrades.web.administration;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class FiliereModel {

	private Long id;

	@NotBlank(message = "Le nom est obligatoire.")
	@Size(max = 50, message = "50 caractères maximum.")
	private String nom;

	private Long nbSemestres;
	
	/**
	 * constructeur par défaut pour initialisation de formulaire.
	 */
	public FiliereModel() {}

	/**
	 * contructeur pour créer une nouvelle filière
	 * @param nom
	 */
	public FiliereModel(String nom) {
		this.nom = nom;
	}

	/**
	 * constructeur pour lister les filières existantes.
	 * @param id
	 * @param nom
	 * @param nbSemestres
	 */
	public FiliereModel(Long id, String nom) {
		this.id = id;
		this.nom = nom;
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

	public Long getNbSemestres() {
		return nbSemestres;
	}

	public void setNbSemestres(Long nbSemestres) {
		this.nbSemestres = nbSemestres;
	}

}
