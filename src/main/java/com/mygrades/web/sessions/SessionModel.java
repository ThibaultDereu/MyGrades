package com.mygrades.web.sessions;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class SessionModel {

	private Long id;
	@NotNull(message = "Semestre obligatoire")
	private Long idSemestre;
	@NotBlank(message = "Le nom de la session doit être renseigné.")
	private String nom;
	private LocalDateTime dateOuverture;
	private LocalDateTime dateCloture;
	private Integer numeroSession;
	private Boolean actif;
	
	private String nomFiliere;
	private String nomSemestre;

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

	public LocalDateTime getDateOuverture() {
		return dateOuverture;
	}

	public void setDateOuverture(LocalDateTime dateOuverture) {
		this.dateOuverture = dateOuverture;
	}

	public LocalDateTime getDateCloture() {
		return dateCloture;
	}

	public void setDateCloture(LocalDateTime dateCloture) {
		this.dateCloture = dateCloture;
	}

	public Integer getNumeroSession() {
		return numeroSession;
	}

	public void setNumeroSession(Integer numeroSession) {
		this.numeroSession = numeroSession;
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	public String getNomSemestre() {
		return nomSemestre;
	}

	public void setNomSemestre(String nomSemestre) {
		this.nomSemestre = nomSemestre;
	}

	public Long getIdSemestre() {
		return idSemestre;
	}

	public void setIdSemestre(Long idSemestre) {
		this.idSemestre = idSemestre;
	}

	public String getNomFiliere() {
		return nomFiliere;
	}

	public void setNomFiliere(String nomFiliere) {
		this.nomFiliere = nomFiliere;
	}

}
