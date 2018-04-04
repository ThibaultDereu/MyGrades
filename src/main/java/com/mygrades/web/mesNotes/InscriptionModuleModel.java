package com.mygrades.web.mesNotes;

import java.util.HashSet;
import java.util.Set;

public class InscriptionModuleModel {

	private Long id;
	private String codeModule;
	private String nomModule;
	private Integer coefficient;
	private CalculateurModel calculateur;
	private Set<InscriptionDevoirModel> inscriptionsDevoir = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodeModule() {
		return codeModule;
	}

	public void setCodeModule(String codeModule) {
		this.codeModule = codeModule;
	}

	public String getNomModule() {
		return nomModule;
	}

	public void setNomModule(String nomModule) {
		this.nomModule = nomModule;
	}

	public CalculateurModel getCalculateur() {
		return calculateur;
	}

	public void setCalculateur(CalculateurModel calculateur) {
		this.calculateur = calculateur;
	}

	public Integer getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Integer coefficient) {
		this.coefficient = coefficient;
	}

	public Set<InscriptionDevoirModel> getInscriptionsDevoir() {
		return inscriptionsDevoir;
	}

	public void setInscriptionsDevoir(Set<InscriptionDevoirModel> inscriptionsDevoir) {
		this.inscriptionsDevoir = inscriptionsDevoir;
	}

}
