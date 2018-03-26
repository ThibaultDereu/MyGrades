package com.mygrades.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;


@Entity
public class Etudiant {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	@NotNull
	private String numero;
	
	@OneToMany(mappedBy = "etudiant", cascade=CascadeType.ALL, orphanRemoval = true)
	private List<InscriptionSession> inscriptionsSession = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utilisateur_id", nullable = false)
	private Utilisateur utilisateur;
	
	protected Etudiant() {}
	
	public Etudiant(String numero, Utilisateur utilisateur) {
		this.numero = numero;
		this.utilisateur = utilisateur;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}
	
	public List<InscriptionSession> getInscriptionsSession() {
		return this.inscriptionsSession;
	}
	
	void ajouterInscriptionSession(InscriptionSession inscriptionSession) {
		this.inscriptionsSession.add(inscriptionSession);
	}
	
	void supprimerInscriptionSession(InscriptionSession inscriptionSession) {
		this.inscriptionsSession.remove(inscriptionSession);
	}
}
