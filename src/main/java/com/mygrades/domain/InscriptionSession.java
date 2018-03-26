package com.mygrades.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
public class InscriptionSession extends AbstractInscription {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Double note;
	private boolean termine;
	private boolean acquis;
	
	@ManyToOne(optional = false)
	private Session session;
	
	@ManyToOne(cascade=CascadeType.MERGE, optional = false)
	@JoinColumn(name="etudiant_id")
	private Etudiant etudiant;
	
	@OneToOne
	private Semestre semestre;
	
	@OneToOne(mappedBy = "inscription", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Calculateur calculateur;
	
	@OneToMany(mappedBy="inscriptionSession", orphanRemoval=true, cascade=CascadeType.ALL)
	private Set<InscriptionModule> inscriptionsModule = new HashSet<>();
	
	protected InscriptionSession() {}
	
	public InscriptionSession(Session session, Etudiant etudiant) {

		// contrôles préliminaires
		this.controlerInscription(session, etudiant);

		this.session = session;
		this.session.ajouterInscriptionSession(this);
		this.semestre = session.getSemestre();
		this.etudiant = etudiant;
		this.calculateur = new Calculateur(this);
		this.etudiant.ajouterInscriptionSession(this);

		// récupérer la dernière inscription de l'étudiant à ce semestre,
		// pour pouvoir le réinscrire aux mêmes modules. Cela permet de recréer les
		// inscriptions sessions de rattrapage, mais aussi les inscriptions sessions non
		// acquises au rattrapage.
		List<InscriptionSession> inscriptionsSession = this.etudiant.getInscriptionsSession();
		InscriptionSession lastInscriptionSession = null;
		LocalDateTime lastDateSession = LocalDateTime.now();

		for (InscriptionSession inscS : inscriptionsSession) {
			if (inscS.getSemestre() == this.semestre) {
				LocalDateTime dateSession = inscS.getSession().getDateOuverture();
				if (dateSession.isBefore(lastDateSession)) {
					lastDateSession = dateSession;
					lastInscriptionSession = inscS;
				}
			}
		}

		Map<Module, InscriptionModule> mapLastInscriptionsModule = new HashMap<>();

		if (lastInscriptionSession != null) {
			for (InscriptionModule inscM : lastInscriptionSession.getInscriptionsModule()) {
				mapLastInscriptionsModule.put(inscM.getModule(), inscM);
			}
		}

		// inscrire d'office l'étudiant aux modules obligatoires
		// ou faisant partie de la session précédente.
		for (Module module : this.semestre.getModules()) {
			InscriptionModule lastInscM = mapLastInscriptionsModule.get(module);

			if (lastInscM != null) {
				if (lastInscM.isAcquis()) {
					// Le module était dans la dernière session et est acquis.
					InscriptionModule inscM = new InscriptionModule(this, lastInscM);
				} else if (this.session.getNumeroSession() > 1 && !lastInscM.isRattrapable()) {
					// Le module était dans la dernière session st ne peut être rattrapé
					// car il s'agit d'une session de rattrapage et le module est non rattrapable
					InscriptionModule inscM = new InscriptionModule(this, lastInscM);
				} else {
					// Le module a été suivi mais pas acquis.
					InscriptionModule inscM = new InscriptionModule(this, module);
				}
			} else if (!module.isOptionnel()) {
				// le module est obligatoire.
				InscriptionModule inscM = new InscriptionModule(this, module);
			}
		}

	}

	private void controlerInscription(Session session, Etudiant etudiant) {

		// impossible d'inscrire un étudiant si la session est inactive
		if (!session.isActif()) {
			throw new IllegalStateException("Inscription impossible sur une session inactive.");
		}

		for (InscriptionSession inscS : etudiant.getInscriptionsSession()) {
			if (inscS.getSemestre() == session.getSemestre()) {
				// impossible d'inscrire un étudiant s'il a déjà acquis le semestre
				if (inscS.isAcquis()) {
					throw new IllegalStateException("L'étudiant a déjà acquis ce semestre.");
				}
				// impossible d'inscrire un étudiant s'il à déjà une inscription en cours à ce
				// semestre
				if (!inscS.isTermine()) {
					throw new IllegalStateException("L'étudiant a déjà une inscription à ce semestre en cours.");
				}
			}
		}
	}

	@Override
	public Double getNote() {
		return this.note;
	}

	@Override
	public boolean isTermine() {
		return this.termine;
	}

	@Override
	public boolean isAcquis() {
		return this.acquis;
	}

	public Etudiant getEtudiant() {
		return etudiant;
	}

	public Set<InscriptionModule> getInscriptionsModule() {
		return this.inscriptionsModule;
	}

	void ajouterInscriptionModule(InscriptionModule inscM) {
		this.inscriptionsModule.add(inscM);
		this.calculateur.refresh();
	}

	public void retirerInscriptionModule(InscriptionModule inscM) {

		if (inscM.isTermine()) {
			throw new IllegalArgumentException("Impossible de retirer une inscription clôturée.");
		}
		if (!inscM.getModule().isOptionnel()) {
			throw new IllegalArgumentException("Un module obligatoire ne peut pas être retiré.");
		}
		for (InscriptionDevoir inscD : inscM.getInscriptionsDevoir()) {
			if (inscD.isTermine()) {
				throw new IllegalArgumentException(
						"Impossible de retirer cette inscription module car elle contient un devoir noté.");
			}
		}

		this.inscriptionsModule.remove(inscM);
	}

	public Semestre getSemestre() {
		return this.semestre;
	}

	public Session getSession() {
		return this.session;
	}

	@Override
	public Set<InscriptionModule> getEnfants() {
		return this.getInscriptionsModule();
	}

	@Override
	public AbstractInscription getParent() {
		return null;
	}

	@Override
	public int getCoefficient() {
		return 1;
	}

	@Override
	public Double getNoteSeuil() {
		return this.semestre.getNoteEliminatoire();
	}

	@Override
	public boolean isRattrapable() {
		return false;
	}

	@Override
	public Calculateur getCalculateur() {
		return this.calculateur;
	}

	public String toString() {
		return String.format("inscription au semestre %s", this.semestre);
	}

	/**
	 * Vérifier si une inscription session peut être clôturée.
	 */
	public boolean isCloturable() {
		// si le calculateur de l'inscription session a pu calculer la note réelle,
		// cela veut dire qu'elle est clôturable.
		if (this.calculateur.getNoteReelle() == null) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Clôture d'inscription session. Cette méthode reste package private, car seule
	 * une session peut l'appeler.
	 */
	void cloturer() {

		for (InscriptionModule inscM : this.inscriptionsModule) {
			inscM.cloturer();
		}

		this.note = this.calculateur.getNoteReelle();
		this.termine = true;
		this.acquis = this.calculateur.getAcquisReel();

	}

	public Long getId() {
		return id;
	}
}
