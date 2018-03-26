package com.mygrades.web.administration;

import org.hibernate.validator.constraints.NotBlank;

public class SemestreModel {

	private Long id;

	@NotBlank(message = "Le semestre doit avoir un nom.")
	private String nom;
	private Integer nbModules;

	public SemestreModel() {
	}

	public SemestreModel(Long id, String nom) {
		this.id = id;
		this.nom = nom;
	}

	public Integer getNbModules() {
		return nbModules;
	}

	public void setNbModules(Integer nbModules) {
		this.nbModules = nbModules;
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

}
