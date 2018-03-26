package com.mygrades.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nom;
	private LocalDateTime dateOuverture;
	private LocalDateTime dateCloture;
	private Integer numeroSession;
	private boolean actif;

	@ManyToOne
	private Semestre semestre;

	@OneToMany(mappedBy = "session", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Devoir> devoirs = new HashSet<>();

	@OneToMany(mappedBy = "session", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<InscriptionSession> inscriptionsSession = new HashSet<>();

	protected Session() {
	}

	/**
	 * Constructeur de session initiale.
	 */
	public Session(Semestre semestre, String nom) {
		this.dateOuverture = LocalDateTime.now();
		this.numeroSession = 1;
		this.actif = true;
		this.semestre = semestre;
		this.nom = nom;
	}

	/**
	 * Constructeur de session de rattrapage.
	 */
	public Session(Session session) {
		this.dateOuverture = LocalDateTime.now();
		this.numeroSession = session.getNumeroSession() + 1;
		this.actif = true;
		this.semestre = session.getSemestre();
		this.nom = session.nom;
	}

	public Set<InscriptionSession> getInscriptionsSession() {
		return inscriptionsSession;
	}

	public LocalDateTime getDateOuverture() {
		return dateOuverture;
	}

	public LocalDateTime getDateCloture() {
		return dateCloture;
	}

	public Long getId() {
		return id;
	}

	public Integer getNumeroSession() {
		return numeroSession;
	}

	public Boolean isActif() {
		return actif;
	}

	public Semestre getSemestre() {
		return this.semestre;
	}

	public Set<Devoir> getDevoirs() {
		return this.devoirs;
	}

	/**
	 * méthode appelée uniquement par la classe Devoir. C'est pour cela qu'elle
	 * reste package private. (et pas publique)
	 */
	void ajouterDevoir(Devoir devoir) {
		this.devoirs.add(devoir);

		// inscrire à ce devoir tous les étudiants déjà inscrits à ce module
		for (InscriptionSession inscS : this.inscriptionsSession) {
			for (InscriptionModule inscM : inscS.getInscriptionsModule()) {
				if (inscM.getModule() == devoir.getModule() && !inscM.isTermine()) {
					InscriptionDevoir inscD = new InscriptionDevoir(inscM, devoir);
				}
			}
		}
	}

	public void retirerDevoir(Devoir devoir) {
		this.devoirs.remove(devoir);

		// supprimer les inscriptions à ce devoir
		for (InscriptionSession inscS : this.inscriptionsSession) {
			for (InscriptionModule inscM : inscS.getInscriptionsModule()) {
				if (inscM.getModule() == devoir.getModule()) {
					for (InscriptionDevoir inscD : inscM.getInscriptionsDevoir()) {
						if (inscD.getDevoir() == devoir) {
							inscM.retirerDevoir(inscD);
							inscD = null;
							break;
						}
					}
				}
			}
		}

		devoir = null;
	}

	/**
	 * méthode appelée uniquement par la classe InscriptionSession. C'est pour cela
	 * qu'elle reste package private. (et pas publique)
	 */
	void ajouterInscriptionSession(InscriptionSession inscS) {
		this.inscriptionsSession.add(inscS);
	}

	public void retirerInscriptionSession(InscriptionSession inscS) {
		// contrôler que l'étudiant n'a participé à aucun devoir.
		for (InscriptionModule inscM : inscS.getInscriptionsModule()) {
			for (InscriptionDevoir inscD : inscM.getInscriptionsDevoir()) {
				if (inscD.isTermine()) {
					throw new IllegalArgumentException(
							"Désincription impossible : cette inscription session contient déjà au moins un devoir noté.");
				}
			}
		}
		
		inscS.getEtudiant().supprimerInscriptionSession(inscS);
		this.inscriptionsSession.remove(inscS);
				
	}

	public void cloturer() {

		// si une seule inscription session n'est pas clôturable,
		// impossible de clôturer la session
		for (InscriptionSession inscS : this.inscriptionsSession) {
			if (!inscS.isCloturable()) {
				throw new IllegalStateException(
						"Au moins une inscription session est non clôturable. Vérifiez que tous les devoirs sont notés.");
			}
		}

		// Clôturer chaque inscription session une par une
		for (InscriptionSession inscS : this.inscriptionsSession) {
			inscS.cloturer();
		}

		this.actif = false;

		if (this.numeroSession == 1) {
			this.rattraper();
		}

	}

	/**
	 * Générer une session de rattrapage. Cela se fait uniquement de manière
	 * automatique, donc la méthode reste privée. On crée une nouvelle session
	 * contenant toutes les inscriptions sessions non acquises.
	 */
	private void rattraper() {

		if (this.actif) {
			throw new IllegalStateException("impossible de rattraper une session encore active");
		}

		// on utilise le constructeur de session de rattrapage.
		Session sess2 = new Session(this);

		for (InscriptionSession inscS : this.getInscriptionsSession()) {
			if (!inscS.isAcquis()) {
				InscriptionSession inscS2 = new InscriptionSession(sess2, inscS.getEtudiant());
			}
		}

	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
}
