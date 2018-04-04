package com.mygrades.web.mesNotes;

import java.util.HashSet;
import java.util.Set;

public class InscriptionSessionModel {

	private long id;
	private String nomSemestre;
	private String nomSession;
	private CalculateurModel calculateur;
	private Set<InscriptionModuleModel> inscriptionsModule = new HashSet<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNomSemestre() {
		return nomSemestre;
	}

	public void setNomSemestre(String nomSemestre) {
		this.nomSemestre = nomSemestre;
	}

	public String getNomSession() {
		return nomSession;
	}

	public void setNomSession(String nomSession) {
		this.nomSession = nomSession;
	}

	public CalculateurModel getCalculateur() {
		return calculateur;
	}

	public void setCalculateur(CalculateurModel calculateur) {
		this.calculateur = calculateur;
	}

	public Set<InscriptionModuleModel> getInscriptionsModule() {
		return inscriptionsModule;
	}

	public void setInscriptionsModule(Set<InscriptionModuleModel> inscriptionsModule) {
		this.inscriptionsModule = inscriptionsModule;
	}

}
