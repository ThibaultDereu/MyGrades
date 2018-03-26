package com.mygrades.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Filiere {
		
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String nom;
	
	@OneToMany(mappedBy="filiere", orphanRemoval=true, cascade=CascadeType.ALL)
	private List<Semestre> semestres = new ArrayList<>();

	/**
	 *  constructeur par défaut pour Hibernate
	 */
	public Filiere() {}
	
	public Filiere(String nom) {
		this.nom = nom;
	}

	public Long getId() {
		return id;
	}
	
	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Semestre> getSemestres() {
		return Collections.unmodifiableList(this.semestres);
	}

	void ajouterSemestre(Semestre semestre) {
		this.semestres.add(semestre);
	}

	public void retirerSemestre(Semestre semestre) {
		this.semestres.remove(semestre);		
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
}
