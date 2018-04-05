package com.mygrades.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mygrades.domain.AbstractInscription;
import com.mygrades.domain.Calculateur;
import com.mygrades.domain.InscriptionDevoir;
import com.mygrades.domain.InscriptionModule;
import com.mygrades.domain.InscriptionSession;
import com.mygrades.repositories.CalculateurRepository;
import com.mygrades.repositories.InscriptionSessionRepository;
import com.mygrades.web.simulateur.CalculateurModel;
import com.mygrades.web.simulateur.InscriptionDevoirModel;
import com.mygrades.web.simulateur.InscriptionModuleModel;
import com.mygrades.web.simulateur.InscriptionSessionModel;
import com.mygrades.web.simulateur.InscriptionSessionSimpleModel;

@Service
public class SimulateurService {

	@Autowired
	InscriptionSessionRepository repInscriptionSession;

	@Autowired
	CalculateurRepository repCalculateur;

	public InscriptionSessionModel getInscriptionComplete(Long idInscriptionSession) {

		// InscriptionSession inscS =
		// repInscriptionSession.findOne(idInscriptionSession);
		InscriptionSession inscS = repInscriptionSession.getInscriptionSessionComplete(idInscriptionSession);
		InscriptionSessionModel inscSM = new InscriptionSessionModel();

		inscSM.setId(inscS.getId());
		inscSM.setNomSemestre(inscS.getSemestre().getNom());
		inscSM.setNomSession(inscS.getSession().getNom());
		inscSM.setPrenomEtudiant(inscS.getEtudiant().getUtilisateur().getPrenom());
		inscSM.setNomEtudiant(inscS.getEtudiant().getUtilisateur().getNom());
		inscSM.setCoefficient(inscS.getCoefficient());
		inscSM.setCalculateur(genererCalculateurModel(inscS.getCalculateur()));

		Set<InscriptionModuleModel> setInscMM = new HashSet<>();

		// créer des comparateurs pour trier les inscriptions module et devoir.
		Comparator<InscriptionModuleModel> inscMMComparator = (InscriptionModuleModel i1,
				InscriptionModuleModel i2) -> i1.getCodeModule().compareTo(i2.getCodeModule());

		Comparator<InscriptionDevoirModel> inscDMComparator = (InscriptionDevoirModel i1,
				InscriptionDevoirModel i2) -> i1.getNomDevoir().compareTo(i2.getNomDevoir());

		for (InscriptionModule inscM : inscS.getInscriptionsModule()) {

			InscriptionModuleModel inscMM = new InscriptionModuleModel();
			inscMM.setId(inscM.getId());
			inscMM.setCodeModule(inscM.getModule().getCode());
			inscMM.setNomModule(inscM.getModule().getNom());
			inscMM.setCoefficient(inscM.getCoefficient());
			inscMM.setCalculateur(genererCalculateurModel(inscM.getCalculateur()));
			Set<InscriptionDevoirModel> setInscDM = new HashSet<>();

			for (InscriptionDevoir inscD : inscM.getInscriptionsDevoir()) {

				InscriptionDevoirModel inscDM = new InscriptionDevoirModel();
				inscDM.setId(inscDM.getId());
				inscDM.setCoefficient(inscD.getCoefficient());
				inscDM.setNomDevoir(inscD.getDevoir().getNom());
				inscDM.setCalculateur(genererCalculateurModel(inscD.getCalculateur()));

				setInscDM.add(inscDM);

			}
			
			// trier les inscriptionDevoirModel et les ajouter à inscriptionModuleModel
			List<InscriptionDevoirModel> listInscDM = new ArrayList<>(setInscDM);
			listInscDM.sort(inscDMComparator);
			inscMM.setInscriptionsDevoir(listInscDM);
			
			setInscMM.add(inscMM);

		}
		
		// trier les inscriptionModuleModel et les ajouter à inscriptionSessionModel
		List<InscriptionModuleModel> listInscMM = new ArrayList<>(setInscMM);
		listInscMM.sort(inscMMComparator);
		inscSM.setInscriptionsModule(listInscMM);

		return inscSM;

	}

	/**
	 * Modifier une note objectif, puis renvoyer tous les calculateurs faisant
	 * partie de la même inscriptionSession
	 */
	@Transactional
	public Set<CalculateurModel> setNoteObjectif(Long idCalculateur, Double noteObjectif) {

		Calculateur calc = repCalculateur.findOne(idCalculateur);

		calc.setNoteObjectif(noteObjectif);

		AbstractInscription racine = calc.getInscription();

		while (racine.getParent() != null) {
			racine = racine.getParent();
		}

		Set<CalculateurModel> calculateursModel = getCalculateurs(racine);

		return calculateursModel;

	}

	public Set<CalculateurModel> getCalculateurs(Long idInscriptionSession) {

		InscriptionSession inscS = repInscriptionSession.findOne(idInscriptionSession);
		return getCalculateurs(inscS);

	}

	private Set<CalculateurModel> getCalculateurs(AbstractInscription insc) {

		Set<CalculateurModel> calculateursModel = new HashSet<>();
		parcourirCalculateurs(insc, calculateursModel);

		return calculateursModel;

	}

	private CalculateurModel genererCalculateurModel(Calculateur calc) {

		CalculateurModel calcM = new CalculateurModel();
		calcM.setAcquisReel(calc.getAcquisReel());
		calcM.setId(calc.getId());
		calcM.setNoteMax(calc.getNoteMax());
		calcM.setNoteMin(calc.getNoteMin());
		calcM.setNoteObjectif(calc.getNoteObjectif());
		calcM.setNoteReelle(calc.getNoteReelle());

		return calcM;

	}

	private void parcourirCalculateurs(AbstractInscription insc, Set<CalculateurModel> calculateursModel) {

		CalculateurModel calcM = genererCalculateurModel(insc.getCalculateur());
		calculateursModel.add(calcM);

		for (AbstractInscription inscEnfant : insc.getEnfants()) {
			parcourirCalculateurs(inscEnfant, calculateursModel);
		}

	}

	@Transactional
	public Set<CalculateurModel> annulerNoteObjectif(Long idCalculateur) {

		return setNoteObjectif(idCalculateur, null);

	}

	public List<InscriptionSessionSimpleModel> getInscriptionsSessionSimples() {
		
		List<InscriptionSessionSimpleModel> inscSM = repInscriptionSession.getInscriptionsSessionSimples();
		return inscSM;

	}

}
