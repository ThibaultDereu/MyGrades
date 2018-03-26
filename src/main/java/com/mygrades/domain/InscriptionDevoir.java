package com.mygrades.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class InscriptionDevoir extends AbstractInscription {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Double note;
	private boolean termine;
	
	@ManyToOne
	private Devoir devoir;
	
	@ManyToOne(optional = false, cascade=CascadeType.MERGE)
	private InscriptionModule inscriptionModule;
	
	@OneToOne(mappedBy = "inscription", cascade=CascadeType.ALL, fetch=FetchType.LAZY, optional = false, orphanRemoval=true)
	private Calculateur calculateur;

	protected InscriptionDevoir() {}
	
	public InscriptionDevoir(InscriptionModule inscM, Devoir devoir) {

		if (inscM.isTermine()) {
			throw new IllegalStateException("Impossible d'inscrire un devoir sur une inscription module clôturée.");
		}

		this.termine = false;
		this.inscriptionModule = inscM;
		this.devoir = devoir;
		this.calculateur = new Calculateur(this);
		inscM.ajouterDevoir(this);
	}

	@Override
	public Double getNote() {
		return this.note;
	}

	public void setNote(double note) {
		controlerSession();
		this.note = note;
		this.termine = true;
		this.calculateur.refresh();
	}

	public void annulerNote() {
		controlerSession();
		this.note = null;
		this.termine = false;
		this.calculateur.refresh();
	}
	
	public Devoir getDevoir() {
		return devoir;
	}

	public InscriptionModule getInscriptionModule() {
		return inscriptionModule;
	}

	@Override
	public Calculateur getCalculateur() {
		return this.calculateur;
	}

	@Override
	public boolean isTermine() {
		return this.termine;
	}

	@Override
	public Set<? extends AbstractInscription> getEnfants() {
		return new HashSet<>();
	}

	@Override
	public AbstractInscription getParent() {
		return this.inscriptionModule;
	}

	@Override
	public int getCoefficient() {
		return this.devoir.getCoefficient();
	}

	@Override
	public Double getNoteSeuil() {
		return 0d;
	}

	@Override
	public boolean isAcquis() {
		return true;
	}

	@Override
	public boolean isRattrapable() {
		return false;
	}
	
	private void controlerSession() {
		// contrôler que la session est active
		if (!this.getInscriptionModule().getInscriptionSession().getSession().isActif()) {
			throw new IllegalStateException("Operation impossible sur une session inactive.");
		}
	}
	
	public String toString() {
		return String.format("inscription du devoir %s", this.devoir);
	}

	public Long getId() {
		return id;
	}

}
