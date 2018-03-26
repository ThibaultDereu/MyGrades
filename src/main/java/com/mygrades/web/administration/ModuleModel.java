package com.mygrades.web.administration;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public class ModuleModel {

	private Long id;
	
	@Size(min = 4, max = 8, message = "Saisissez un code module entre 4 et 8 caractères.")
	private String code;
	
	@NotBlank(message = "Le nom du module doit être renseigné.")
	@Size(max = 255)
	private String nom;
	
	@NotNull(message = "Le nombre de crédits doit être renseigné")
	@Min(value = 1, message = "Saisissez un nombre entier positif non nul.")
	private Integer nbCredits;
	private Boolean optionnel = false;
	
	@NotNull(message = "Le coefficient doit être renseigné.")
	@Min(value = 1, message = "Saisissez un nombre entier positif non nul.")
	private Integer coefficient;
	private Boolean rattrapable = true;
	
	@NotNull(message = "La note seuil doit être renseignée.")
	@Range(min = 0, max = 10, message = "La note seuil doit être comprise entre 0 et 10")
	private Double noteSeuil;
	
	private Long idEnseignant;
	private String nomEnseignant;
	private String prenomEnseignant;

	public ModuleModel() {
	}

	public ModuleModel(Long id, String code) {
		this.id = id;
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Integer getNbCredits() {
		return nbCredits;
	}

	public void setNbCredits(Integer nbCredits) {
		this.nbCredits = nbCredits;
	}

	public Boolean getOptionnel() {
		return optionnel;
	}

	public void setOptionnel(Boolean optionnel) {
		this.optionnel = optionnel;
	}

	public Integer getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Integer coefficient) {
		this.coefficient = coefficient;
	}

	public Boolean getRattrapable() {
		return rattrapable;
	}

	public void setRattrapable(Boolean rattrapable) {
		this.rattrapable = rattrapable;
	}

	public Double getNoteSeuil() {
		return noteSeuil;
	}

	public void setNoteSeuil(Double noteSeuil) {
		this.noteSeuil = noteSeuil;
	}

	public String getNomEnseignant() {
		return nomEnseignant;
	}

	public void setNomEnseignant(String nomEnseignant) {
		this.nomEnseignant = nomEnseignant;
	}

	public String getPrenomEnseignant() {
		return prenomEnseignant;
	}

	public void setPrenomEnseignant(String prenomEnseignant) {
		this.prenomEnseignant = prenomEnseignant;
	}

	public Long getIdEnseignant() {
		return idEnseignant;
	}

	public void setIdEnseignant(Long idEnseignant) {
		this.idEnseignant = idEnseignant;
	}

}
