package com.mygrades.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Le calculateur contient les attributs noteReelle et acquisReel, calculés à
 * partir des notes réelles des sous-composants s’il y en a. Au moment de la
 * clôture de l’inscription liée à ce calculateur, l’inscription doit juste
 * récupérer ces attributs et mettre à jour son propre état à partir de ceux-ci.
 * 
 * Dans le cadre de la simulation (calcul des notes en fonction d’objectifs), le
 * calculateur permet également de déterminer une note minimale et une note
 * maximale. Le calcul des notes minimales et maximales d’un calculateur se fait
 * en fonction des notes réelles et des notes objectif des autres calculateurs.
 */
@Entity
public class Calculateur {
	/*
	 * Les modifications sur des inscriptions qui peuvent entraîner la
	 * réactualisation du calculateur sont:
	 * 
	 * - création de l'inscription => le calculateur est notifié parce qu'il est
	 * créé en même temps.
	 * 
	 * - retrait d'une inscription enfant => l'inscription n'est pas terminé : donc
	 * on récupère les notes des enfants.
	 * 
	 * - changement de la note => le calculateur peut le savoir en comparant
	 * l'ancienne note avec la nouvelle.
	 * 
	 * - changement du coefficient d'un devoir.
	 * 
	 * - changement de la note objectif.
	 * 
	 */

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	private AbstractInscription inscription;
	private Double noteReelle;
	private Double noteObjectif;
	private Double noteAcquiseMin;
	private Double noteAcquiseMax;
	private Double noteRequiseMin;
	private Double noteRequiseMax;
	private Integer sommeCoeff;
	private Boolean acquisReel;
	
	protected Calculateur() {}
	
	public Long getId() {
		return this.id;
	}
	
	public AbstractInscription getInscription() {
		return inscription;
	}

	public Calculateur(AbstractInscription inscription) {
		this.inscription = inscription;
		this.noteAcquiseMin = 0d;
		this.noteAcquiseMax = 20d;
		this.noteRequiseMin = 0d;
		this.noteRequiseMax = 20d;
	}

	public Double getNoteReelle() {
		return noteReelle;
	}

	public Double getNoteObjectif() {
		return noteObjectif;
	}

	/**
	 * Modifier la note objectif pour cette inscription. Pour annuler un objectif,
	 * passer en paramètre la valeur null.
	 */
	public void setNoteObjectif(Double noteObjectif) {
		this.noteObjectif = noteObjectif;

		// le changement de note objectif change les notes acquises des parents
		AbstractInscription inscParent = this.inscription.getParent();
		if (inscParent != null) {
			inscParent.getCalculateur().calculerNotesAcquises();
		}

		// le changement de note objectif change les notes requises des enfants.
		for (AbstractInscription inscEnfant : this.inscription.getEnfants()) {
			inscEnfant.getCalculateur().calculerNotesRequises();
		}
	}

	/**
	 * Récupérer le statut de l'inscription. Renvoie true si l'inscription est
	 * acquise, false si l'inscription n'est pas acquise, et null si le résultat ne
	 * peut pas encore être déterminé.
	 */
	public Boolean getAcquisReel() {
		return acquisReel;
	}

	/**
	 * méthode à appeler pour déclencher le recalcul des notes acquises.
	 */
	public void refresh() {
		this.calculerNotesAcquises();
	}

	/**
	 * Calcul de la note minimale acquise et de la note maximale acquise. Méthode
	 * privée, car seul un calculateur peut appeler cette méthode. Cette méthode
	 * calcule aussi la note réelle.
	 */
	private void calculerNotesAcquises() {

		Double oldAcqMax = this.noteAcquiseMax;
		Double oldAcqMin = this.noteAcquiseMin;
		Double oldReel = this.noteReelle;

		if (this.inscription.isTermine()) {
			// l'inscription est terminée => on récupère sa note
			this.noteReelle = this.inscription.getNote();
			this.noteAcquiseMin = this.noteReelle;
			this.noteAcquiseMax = this.noteReelle;
		} else {
			// calcul à partir des inscriptions enfants
			this.sommeCoeff = 0;
			int coeff = 0;
			double sommeAcquisMin = 0;
			double sommeAcquisMax = 0;
			double sommeReel = 0;
			boolean termineReel = true;

			for (AbstractInscription insc : this.inscription.getEnfants()) {
				// calculer les sommes des notes acquises des enfants
				coeff = insc.getCoefficient();
				this.sommeCoeff += coeff;
				Calculateur calcEnfant = insc.getCalculateur();
				if (calcEnfant.getNoteObjectif() != null) {
					// on compte la note objectif comme de l'acquis si elle existe
					sommeAcquisMin += calcEnfant.getNoteObjectif() * coeff;
					sommeAcquisMax += calcEnfant.getNoteObjectif() * coeff;
				} else {
					sommeAcquisMin += calcEnfant.noteAcquiseMin * coeff;
					sommeAcquisMax += calcEnfant.noteAcquiseMax * coeff;
				}

				// pour calcul de la note réelle
				if (calcEnfant.getNoteReelle() == null) {
					termineReel = false;
				} else if (termineReel) {
					sommeReel += calcEnfant.getNoteReelle() * coeff;
				}
			}

			if (this.sommeCoeff == 0) {
				// si l'inscription n'a pas d'enfant (ayant un coefficient).
				this.noteAcquiseMin = 0d;
				this.noteAcquiseMax = 20d;
				this.noteReelle = null;
			} else {
				this.noteAcquiseMin = sommeAcquisMin / this.sommeCoeff;
				this.noteAcquiseMax = Math.min(20d, sommeAcquisMax / this.sommeCoeff);

				// calcul de la note réelle si tous les enfants ont une note réelle
				if (termineReel) {
					this.noteReelle = sommeReel / this.sommeCoeff;
				} else {
					this.noteReelle = null;
				}
			}
		}

		/*
		 * Comme la note acquise d'un calculateur dépend de la note acquise de ses
		 * enfants, lorsqu'un calculateur modifie une note acquise, il déclenche le
		 * recalcul des notes acquises de son parent. (à faire uniquement si les notes
		 * acquises ont changé)
		 */
		if (this.noteAcquiseMax.doubleValue() != oldAcqMax || this.noteAcquiseMin.doubleValue() != oldAcqMin) {
			AbstractInscription inscParent = this.inscription.getParent();
			if (inscParent != null) {
				inscParent.getCalculateur().calculerNotesAcquises();
			}

			/*
			 * Comme la note requise d'un calculateur dépend de la note acquise de son
			 * parent, lorsqu'un calculateur modifie une note acquise, il déclenche le
			 * recalcul des notes requises de tous ses enfants. (à faire uniquement si les
			 * notes acquises ont changé)
			 */
			for (AbstractInscription inscEnfant : this.inscription.getEnfants()) {
				inscEnfant.getCalculateur().calculerNotesRequises();
			}
		}

		/*
		 * Comme les enfants peuvent se compenser en fonction de la note de leur parent,
		 * si la note réelle a changé, il faut recalculer le statut des enfants.
		 */
		if (this.noteReelle != oldReel) {
			for (AbstractInscription inscEnfant : this.inscription.getEnfants()) {
				inscEnfant.getCalculateur().calculerAcquisReel();
			}
		}

		/*
		 * Une fois que la note réelle du calculateur et que la note réelle de son
		 * parent sont calculées, on peut déterminer s'il est acquis ou pas.
		 */
		this.calculerAcquisReel();

	}

	/**
	 * Calcul de la note minimale REquise et de la note maximale REquise. Méthode
	 * privée, car seul un calculateur peut appeler cette méthode.
	 */
	private void calculerNotesRequises() {

		double oldReqMax = this.noteRequiseMax;
		double oldReqMin = this.noteRequiseMin;

		AbstractInscription inscParent = this.inscription.getParent();

		if (this.noteReelle != null) {

			this.noteRequiseMin = 0d;
			this.noteRequiseMax = 20d;

		} else if (inscParent == null || this.inscription.getCoefficient() <= 0) {

			this.noteRequiseMin = 0d;
			this.noteRequiseMax = 20d;

		} else {
			/*
			 * Calculer les notes requises à partir des notes requises du parent.
			 * 
			 * La note requise minimale est la note minimale pour pouvoir atteindre la note
			 * minimale requise du parent, dans le meilleur des scénarios : tous les autres
			 * calculateurs du même parent sont à leur maximum.
			 * 
			 * La note requise maximale est la note qu'il faut atteindre pour arriver à la
			 * note maximale requise du parent, dans le pire des scénarios : tous les autres
			 * calculateurs du même parent sont à leur note minimale.
			 */

			Calculateur calcParent = inscParent.getCalculateur();
			double pointsRequisMin;

			double noteReqMinParent = calcParent.noteObjectif != null ? calcParent.noteObjectif
					: calcParent.noteRequiseMin;
			double noteReqMaxParent = calcParent.noteObjectif != null ? calcParent.noteObjectif
					: calcParent.noteRequiseMax;

			pointsRequisMin = noteReqMinParent * calcParent.sommeCoeff
					- (calcParent.noteAcquiseMax * calcParent.sommeCoeff
							- this.inscription.getCoefficient() * this.noteAcquiseMax);
			this.noteRequiseMin = Math.min(20, pointsRequisMin / this.inscription.getCoefficient());

			double pointsRequisMax;
			pointsRequisMax = noteReqMaxParent * calcParent.sommeCoeff
					- (calcParent.noteAcquiseMin * calcParent.sommeCoeff
							- this.inscription.getCoefficient() * this.noteAcquiseMin);
			this.noteRequiseMax = pointsRequisMax / this.inscription.getCoefficient();

		}

		/*
		 * Comme la note requise d’un calculateur dépend de la note requise de son
		 * parent, lorsqu'un calculateur modifie une note requise, il déclenche le
		 * recalcul des notes requises de tous ses enfants. (à faire uniquement si les
		 * notes requises ont changé)
		 */
		if (this.noteRequiseMax.doubleValue() != oldReqMax || this.noteRequiseMin.doubleValue() != oldReqMin) {
			for (AbstractInscription inscEnfant : this.inscription.getEnfants()) {
				inscEnfant.getCalculateur().calculerNotesRequises();
			}
		}
	}

	/**
	 * Calcul du statut acquis réel. Le résultat est dépendant de la note réelle de
	 * ce calculateur, des notes réelles de ses enfants et de la note réelle de son
	 * parent. acquisReel vaut null s'il ne peut pas être déterminé.
	 */
	private void calculerAcquisReel() {
		// acquisReel ne peut pas être calculé si la note réelle n'est pas encore
		// calculée.
		if (this.noteReelle == null) {
			this.acquisReel = null;
		}
		// la note du calculateur est sous le seuil
		else if (this.noteReelle < this.inscription.getNoteSeuil()) {
			this.acquisReel = false;
		} else {
			// vérifie s'il existe un enfant ayant une note sous le seuil
			boolean enfantSousSeuil = false;
			for (AbstractInscription insc : this.inscription.getEnfants()) {
				if (insc.getCalculateur().getNoteReelle() < insc.getNoteSeuil()) {
					enfantSousSeuil = true;
					break;
				}
			}
			// au moins un enfant a une note réelle sous le seuil
			if (enfantSousSeuil) {
				this.acquisReel = false;
			}
			// la note du calculateur est au-dessus de la moyenne
			else if (this.noteReelle >= 10) {
				this.acquisReel = true;
			}
			// la note du calculateur est sur le seuil mais sous la moyenne, on essaie de
			// compenser
			else if (this.inscription.getParent() != null) {
				Double noteReelleParent = this.inscription.getParent().getCalculateur().getNoteReelle();
				// la note réelle du parent n'existe pas encore, donc on ne sait pas encore si
				// la compensation est possible
				if (noteReelleParent == null) {
					this.acquisReel = null;
				}
				// on peut compenser
				else if (noteReelleParent >= 10) {
					this.acquisReel = true;
				}
				// on ne peut pas compenser
				else {
					this.acquisReel = false;
				}
			}
			// pas de parent : on ne peut pas compenser
			else {
				this.acquisReel = false;
			}
		}

	}

	/**
	 * Retourne la note minimale possible en fonction de toutes les autres notes.
	 */
	public Double getNoteMin() {
		return Math.max(this.noteAcquiseMin, this.noteRequiseMin);
	}

	/**
	 * Retourne la note maximale possible en fonction de toutes les autres notes.
	 */
	public Double getNoteMax() {
		return Math.min(this.noteAcquiseMax, this.noteRequiseMax);
	}

	@Override
	public String toString() {
		return String.format("calculateur de %s", this.getInscription());
	}

}
