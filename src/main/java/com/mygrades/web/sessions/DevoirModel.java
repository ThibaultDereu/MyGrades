package com.mygrades.web.sessions;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class DevoirModel {

	private Long id;
	@NotBlank(message = "Le devoir doit avoir un nom")

	private String nom;

	@NotNull(message = "le coefficient est obligatoire")
	@Min(value = 1, message = "Saisissez un nombre entier positif.")
	private Integer coefficient;

	private Long idModule;
	private String codeModule;
	private String nomModule;
	
	private Long nbInscriptions;
	private Long nbInscriptionsNotees;
	
	public DevoirModel() {}

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

	public Integer getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Integer coefficient) {
		this.coefficient = coefficient;
	}

	public Long getIdModule() {
		return idModule;
	}

	public void setIdModule(Long idModule) {
		this.idModule = idModule;
	}

	public Long getNbInscriptions() {
		return nbInscriptions;
	}

	public void setNbInscriptions(Long nbInscriptions) {
		this.nbInscriptions = nbInscriptions;
	}

	public Long getNbInscriptionsNotees() {
		return nbInscriptionsNotees;
	}

	public void setNbInscriptionsNotees(Long nbInscriptionsNotees) {
		this.nbInscriptionsNotees = nbInscriptionsNotees;
	}

	public String getNomModule() {
		return nomModule;
	}

	public void setNomModule(String nomModule) {
		this.nomModule = nomModule;
	}

	public String getCodeModule() {
		return codeModule;
	}

	public void setCodeModule(String codeModule) {
		this.codeModule = codeModule;
	}

}
