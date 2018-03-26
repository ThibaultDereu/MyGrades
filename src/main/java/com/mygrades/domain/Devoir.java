package com.mygrades.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Devoir {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String nom;
	private Integer coefficient;
	@ManyToOne
	private Session session;
	@OneToOne
	private Module module;
	
	protected Devoir() {}

	public Devoir(Session session, Module module, int coefficient) {
		this.session = session;
		this.module = module;
		this.coefficient = coefficient;
		session.ajouterDevoir(this);
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
		
		// comme le coefficient a changé, les notes liées à ce devoir doivent changer
		// mettre à jour les notes de toutes les inscriptions modules contenant
		for (InscriptionSession inscSess : this.session.getInscriptionsSession()) {
			for (InscriptionModule inscMod : inscSess.getInscriptionsModule()) {
				if (inscMod.getModule() == this.module) {
					inscMod.getCalculateur().refresh();
				}
			}
		}
		
	}

	public Session getSession() {
		return session;
	}
	
	public Module getModule() {
		return module;
	}
	
	public String toString() {
		return String.format("devoir sur le module %s", this.module);
	}

	public Long getId() {
		return id;
	}
}
