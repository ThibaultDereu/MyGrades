package com.mygrades.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
public class Enseignant {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="utilisateur_id", nullable = false)
	private Utilisateur utilisateur;
	
	@OneToMany
	@JoinColumn(name="enseignant_id")
	private List<Module> modules;
	
	protected Enseignant() {}
	
	public Enseignant(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	
	public Long getId() {
		return id;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	
}
