package com.mygrades.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Semestre {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="filiere_id")
	private Filiere filiere;
	private String nom;
	private Integer nbCredits;
	private Double noteEliminatoire = 0d;
	
	@ManyToMany
	private List<Module> modules = new ArrayList<>();
	
	/**
	 * contructeur par défaut pour Hibernate
	 */
	protected Semestre() {}
	
	public Long getId() {
		return this.id;
	}
	
	public Semestre(Filiere filiere, String nom) {
		this.filiere = filiere;
		filiere.ajouterSemestre(this);
		this.nom = nom;
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

	public Double getNoteEliminatoire() {
		return noteEliminatoire;
	}

	public void setNoteEliminatoire(Double noteEliminatoire) {
		this.noteEliminatoire = noteEliminatoire;
	}

	public Filiere getFiliere() {
		return this.filiere;
	}
	
	public List<Module> getModules() {
		return this.modules;
	}
	
	public void addModule(Module module) {
		this.modules.add(module);
	}
	
	public void removeModule(Module module) {
		this.modules.remove(module);
	}
	
	@Override
	public String toString() {
		return super.toString();
		//return this.nom;
	}	
}
