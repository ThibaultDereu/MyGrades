package com.mygrades.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Module {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	@NotNull
	private String code;
	@NotNull
	private String nom;
	private Integer nbCredits;
	private Boolean optionnel = false;
	private Integer coefficient = 1;
	private Boolean rattrapable = true;
	private Double noteSeuil = 0d;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="enseignant_id")
	private Enseignant enseignant;
	
	protected Module() {}

	public Module(String nom, String code) {
		this.nom = nom;
		this.code = code;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public Integer getNbCredits() {
		return nbCredits;
	}

	public void setNbCredits(Integer nbCredits) {
		this.nbCredits = nbCredits;
	}

	public boolean isOptionnel() {
		return optionnel;
	}

	public void setOptionnel(boolean optionnel) {
		this.optionnel = optionnel;
	}

	public Integer getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(int coefficient) {
		if (coefficient <= 0) {
			throw new IllegalArgumentException("Le coefficient d'un module ne peut pas être nul");
		}
		this.coefficient = coefficient;
	}
	
	public Enseignant getEnseignant() {
		return enseignant;
	}

	public void setEnseignant(Enseignant enseignant) {
		this.enseignant = enseignant;
	}
	
	public boolean isRattrapable() {
		return rattrapable;
	}

	public void setRattrapable(boolean rattrapable) {
		this.rattrapable = rattrapable;
	}

	public Double getNoteSeuil() {
		return noteSeuil;
	}

	public void setNoteSeuil(double noteSeuil) {
		this.noteSeuil = noteSeuil;
	}
	
	@Override
	public String toString() {
		return this.nom;
	}
		
}
