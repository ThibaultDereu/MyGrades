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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class InscriptionModule extends AbstractInscription {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Double note;
	private boolean termine;
	private boolean acquis;

	@ManyToOne(optional = false)
	private Module module;
	
	@ManyToOne(optional = false, cascade=CascadeType.MERGE)
	private InscriptionSession inscriptionSession;
	
	@OneToMany(mappedBy="inscriptionModule", orphanRemoval=true, cascade=CascadeType.ALL)
	private Set<InscriptionDevoir> inscriptionsDevoir = new HashSet<>();
	
	@OneToOne(mappedBy = "inscription", cascade=CascadeType.ALL, fetch=FetchType.LAZY, optional = false, orphanRemoval=true)
	private Calculateur calculateur;

	protected InscriptionModule() {}
	
	/*
	 * Constructeur pour inscription à un module faisant l'objet de devoirs.
	 */
	public InscriptionModule(InscriptionSession inscriptionSession, Module module) {

		// contrôler l'inscription module
		for (InscriptionModule im : inscriptionSession.getInscriptionsModule()) {
			if (im.getModule() == module) {
				throw new IllegalStateException(
						"L'étudiant est déjà inscrit à ce module dans une session active.");
			}
		}

		this.inscriptionSession = inscriptionSession;
		this.module = module;
		this.termine = false;
		this.acquis = false;
		this.inscriptionsDevoir = new HashSet<>();
		this.calculateur = new Calculateur(this);
		inscriptionSession.ajouterInscriptionModule(this);

		// ajouter les devoirs existants déjà dans la session et concernant ce module
		Session session = this.inscriptionSession.getSession();
		for (Devoir devoir : session.getDevoirs()) {
			if (devoir.getModule() == this.module) {
				InscriptionDevoir inscD = new InscriptionDevoir(this, devoir);
			}
		}
	}

	/*
	 * Constructeur pour recopie à partir d'une autre inscription module, pour une
	 * inscription module ne faisant pas l'objet de nouveaux devoirs. (Cas d'une
	 * inscription module acquise, ou d'une inscription module non rattrapable dans
	 * une session de rattrapage)
	 */
	public InscriptionModule(InscriptionSession inscriptionSession, InscriptionModule inscriptionModule) {

		// contrôler l'inscription module
		for (InscriptionModule im : inscriptionSession.getInscriptionsModule()) {
			if (im.getModule() == inscriptionModule.getModule()) {
				throw new IllegalStateException("Une inscription module existe déjà dans cette inscription session.");
			}
		}

		this.inscriptionSession = inscriptionSession;
		this.module = inscriptionModule.getModule();
		this.termine = true;
		this.acquis = inscriptionModule.isAcquis();
		this.calculateur = new Calculateur(this);
		inscriptionSession.ajouterInscriptionModule(this);
	}

	void ajouterDevoir(InscriptionDevoir inscD) {
		this.inscriptionsDevoir.add(inscD);
		this.calculateur.refresh();
	}

	public void retirerDevoir(InscriptionDevoir inscD) {
		if (inscD.isTermine()) {
			throw new IllegalStateException("Impossible de retirer une inscription devoir déjà notée.");
		}
		this.inscriptionsDevoir.remove(inscD);
		inscD = null;
	}

	public Module getModule() {
		return this.module;
	}

	@Override
	public Double getNote() {
		return this.note;
	}

	public InscriptionSession getInscriptionSession() {
		return inscriptionSession;
	}

	public Set<InscriptionDevoir> getInscriptionsDevoir() {
		return inscriptionsDevoir;
	}
	
	public InscriptionDevoir getInscriptionDevoir(Devoir devoir) {
		
		for (InscriptionDevoir inscD : this.inscriptionsDevoir) {
			if (inscD.getDevoir() == devoir) {
				return inscD;
			}
		}
		return null;
	}

	@Override
	public boolean isTermine() {
		return this.termine;
	}

	@Override
	public boolean isAcquis() {
		return this.acquis;
	}

	@Override
	public Set<? extends AbstractInscription> getEnfants() {
		return this.getInscriptionsDevoir();
	}

	@Override
	public AbstractInscription getParent() {
		return this.inscriptionSession;
	}

	@Override
	public int getCoefficient() {
		return this.getModule().getCoefficient();
	}

	@Override
	public Double getNoteSeuil() {
		return this.getModule().getNoteSeuil();
	}

	@Override
	public boolean isRattrapable() {
		return this.getModule().isRattrapable();
	}

	@Override
	public Calculateur getCalculateur() {
		return this.calculateur;
	}
	
	public String toString() {
		return String.format("inscription du module %s", this.module);
	}
	
	/**
	 * Clôture d'inscription session. Cette méthode reste package private, car seule
	 * une inscription session peut l'appeler. 
	 */
	void cloturer() {
		
		this.note = this.calculateur.getNoteReelle();
		this.termine = true;
		this.acquis = this.calculateur.getAcquisReel();
		
	}

	public Long getId() {
		return id;
	}

}
