package com.mygrades.web.simulateur;

import java.util.ArrayList;
import java.util.List;

public class InscriptionSessionModel {

	private long id;
	private String nomSemestre;
	private String nomSession;
	private String prenomEtudiant;
	private String nomEtudiant;
	private Integer coefficient;
	private String nomFiliere;
	private CalculateurModel calculateur;
	private List<InscriptionModuleModel> inscriptionsModule = new ArrayList<>();

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

	public List<InscriptionModuleModel> getInscriptionsModule() {
		return inscriptionsModule;
	}

	public void setInscriptionsModule(List<InscriptionModuleModel> inscriptionsModule) {
		this.inscriptionsModule = inscriptionsModule;
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

	public Integer getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Integer coefficient) {
		this.coefficient = coefficient;
	}

	public String getNomFiliere() {
		return nomFiliere;
	}

	public void setNomFiliere(String nomFiliere) {
		this.nomFiliere = nomFiliere;
	}
}
