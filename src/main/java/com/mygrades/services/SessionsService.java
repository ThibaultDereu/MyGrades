package com.mygrades.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mygrades.domain.Devoir;
import com.mygrades.domain.Etudiant;
import com.mygrades.domain.InscriptionDevoir;
import com.mygrades.domain.InscriptionModule;
import com.mygrades.domain.InscriptionSession;
import com.mygrades.domain.Module;
import com.mygrades.domain.Semestre;
import com.mygrades.domain.Session;
import com.mygrades.domain.Utilisateur;
import com.mygrades.repositories.DevoirRepository;
import com.mygrades.repositories.EtudiantRepository;
import com.mygrades.repositories.FiliereRepository;
import com.mygrades.repositories.InscriptionDevoirRepository;
import com.mygrades.repositories.InscriptionModuleRepository;
import com.mygrades.repositories.InscriptionSessionRepository;
import com.mygrades.repositories.ModuleRepository;
import com.mygrades.repositories.SemestreRepository;
import com.mygrades.repositories.SessionRepository;
import com.mygrades.web.administration.EtudiantModel;
import com.mygrades.web.administration.ModuleModel;
import com.mygrades.web.sessions.DevoirModel;
import com.mygrades.web.sessions.FiliereSessionsModel;
import com.mygrades.web.sessions.InscriptionDevoirModel;
import com.mygrades.web.sessions.InscriptionModuleModel;
import com.mygrades.web.sessions.InscriptionSessionModel;
import com.mygrades.web.sessions.ModuleAvecDevoirsModel;
import com.mygrades.web.sessions.NoteDevoirModel;
import com.mygrades.web.sessions.SessionModel;

@Service
public class SessionsService {

	@Autowired
	FiliereRepository repFiliere;

	@Autowired
	SemestreRepository repSemestre;

	@Autowired
	ModuleRepository repModule;

	@Autowired
	SessionRepository repSession;

	@Autowired
	DevoirRepository repDevoir;

	@Autowired
	EtudiantRepository repEtudiant;

	@Autowired
	InscriptionSessionRepository repInscriptionSession;
	
	@Autowired
	InscriptionModuleRepository repInscriptionModule;
	
	@Autowired
	InscriptionDevoirRepository repInscriptionDevoir;

	public List<FiliereSessionsModel> getAllFilieres() {
		return repFiliere.getFilieresSessions();
	}

	public List<SessionModel> getSessions(Long idFiliere, Boolean actif) {

		List<SessionModel> sessionsModel = new ArrayList<>();
		List<Session> sessions = repSession.getSessions(idFiliere, actif);

		for (Session s : sessions) {
			SessionModel sm = new SessionModel();
			sm.setActif(s.isActif());
			sm.setDateCloture(s.getDateCloture());
			sm.setDateOuverture(s.getDateOuverture());
			sm.setId(s.getId());
			sm.setIdSemestre(s.getSemestre().getId());
			sm.setNom(s.getNom());
			sm.setNomSemestre(s.getSemestre().getNom());
			sm.setNumeroSession(s.getNumeroSession());
			sessionsModel.add(sm);
		}

		return sessionsModel;

	}

	@Transactional
	public void creerSession(SessionModel sessionModel) {

		Semestre semestre = repSemestre.findOne(sessionModel.getIdSemestre());
		Session session = new Session(semestre, sessionModel.getNom());
		repSession.save(session);

	}

	public SessionModel getSession(Long idSession) {

		SessionModel sm = new SessionModel();
		Session s = repSession.findOne(idSession);

		if (s != null) {
			sm.setActif(s.isActif());
			sm.setDateCloture(s.getDateCloture());
			sm.setDateOuverture(s.getDateOuverture());
			sm.setId(s.getId());
			sm.setIdSemestre(s.getSemestre().getId());
			sm.setNom(s.getNom());
			sm.setNomFiliere(s.getSemestre().getFiliere().getNom());
			sm.setNomSemestre(s.getSemestre().getNom());
			sm.setNumeroSession(s.getNumeroSession());
		}

		return sm;

	}

	@Transactional
	public void modifierSession(SessionModel sm) {

		Session s = repSession.findOne(sm.getId());
		s.setNom(sm.getNom());
		repSession.save(s);

	}

	@Transactional
	public void supprimerSession(Long idSession) {

		repSession.delete(idSession);

	}
	    
    
	@Transactional
	public void cloturerSession(Long idSession) {
		
		Session session = repSession.findOne(idSession);
		session.cloturer();
		repSession.save(session);
		
	}
	

    
	public List<ModuleAvecDevoirsModel> getModulesAvecDevoirs(Long idSession) {

		// récupérer les devoirs
		List<Tuple> tuplesDevoirs = repSession.getDevoirsAvecModule(idSession);

		// contruire les modulesAvecDevoirsModel
		// sql a déjà ordonné les modules, donc utilisation de LinkedHashMap pour
		// préserver l'ordre d'insertion.
		Map<Long, ModuleAvecDevoirsModel> mapModulesAvecDevoirs = new LinkedHashMap<>();

		for (Tuple tdev : tuplesDevoirs) {

			Long idModule = tdev.get("idModule", Long.class);
			if (!mapModulesAvecDevoirs.containsKey(idModule)) {

				String codeModule = tdev.get("codeModule", String.class);
				String nomModule = tdev.get("nomModule", String.class);
				ModuleAvecDevoirsModel mad = new ModuleAvecDevoirsModel(idModule, codeModule, nomModule);
				mapModulesAvecDevoirs.put(idModule, mad);

			}

		}

		// remplir les modulesAvecDevoirsModel avec des DevoirModel
		for (Tuple tdev : tuplesDevoirs) {

			if (tdev.get("idDevoir", Long.class) != null) {
				DevoirModel devm = new DevoirModel();
				devm.setId(tdev.get("idDevoir", Long.class));
				devm.setCoefficient(tdev.get("coefficient", Integer.class));
				devm.setNom(tdev.get("nomDevoir", String.class));
				devm.setNbInscriptions(tdev.get("nbInscriptions", Long.class));
				devm.setNbInscriptionsNotees(tdev.get("nbInscriptionsNotees", Long.class));

				Long idModule = tdev.get("idModule", Long.class);
				ModuleAvecDevoirsModel mad = mapModulesAvecDevoirs.get(idModule);
				mad.getDevoirs().add(devm);
			}
		}

		// construction de l'objet à retourner
		List<ModuleAvecDevoirsModel> modulesAvecDevoirs = new ArrayList(mapModulesAvecDevoirs.values());

		return modulesAvecDevoirs;

	}
	
	@Transactional
	public void creerDevoir(Long idSession, DevoirModel devoirModel) {

		Session session = repSession.findOne(idSession);
		Module module = repModule.findOne(devoirModel.getIdModule());

		if (session != null && module != null) {
			Devoir devoir = new Devoir(session, module, devoirModel.getCoefficient());
			devoir.setNom(devoirModel.getNom());
			repSession.save(session);

		}

	}
	


	public void injecteModuleDansDevoir(DevoirModel devoir, Long idModule) {

		Module module = repModule.findOne(idModule);
		devoir.setIdModule(module.getId());
		devoir.setNomModule(module.getNom());

	}

	public DevoirModel getDevoir(Long idDevoir) {

		Devoir devoir = repDevoir.findOne(idDevoir);
		DevoirModel devoirModel = new DevoirModel();

		if (devoir != null) {
			devoirModel.setCoefficient(devoir.getCoefficient());
			devoirModel.setId(devoir.getId());
			devoirModel.setIdModule(devoir.getModule().getId());
			devoirModel.setCodeModule(devoir.getModule().getCode())
;			devoirModel.setNomModule(devoir.getModule().getNom());
			devoirModel.setNom(devoir.getNom());
		}

		return devoirModel;

	}

	@Transactional
	public void modifierDevoir(Long idSession, DevoirModel devoirModel) {

		Session session = repSession.findOne(idSession);
		Devoir devoir = repDevoir.findOne(devoirModel.getId());

		devoir.setCoefficient(devoirModel.getCoefficient());
		devoir.setNom(devoirModel.getNom());
		repSession.save(session);

	}

	@Transactional
	public void supprimerDevoir(Long idSession, DevoirModel devoirModel) {

		Session session = repSession.findOne(idSession);
		Devoir devoir = repDevoir.findOne(devoirModel.getId());

		session.retirerDevoir(devoir);
		repSession.save(session);

	}

	public List<InscriptionSessionModel> getInscriptionsSession(Long idSession) {

		List<Tuple> inscriptions = repSession.getInscriptionsSessions(idSession);

		List<InscriptionSessionModel> inscriptionsModel = new ArrayList<>();

		for (Tuple insc : inscriptions) {

			InscriptionSessionModel inscModel = new InscriptionSessionModel();
			inscModel.setId(insc.get("id", Long.class));
			inscModel.setNbModules(insc.get("nbModules", Long.class));
			inscModel.setNomEtudiant(insc.get("nomEtudiant", String.class));
			inscModel.setNote(insc.get("note", Double.class));
			inscModel.setNumeroEtudiant(insc.get("numeroEtudiant", String.class));
			inscModel.setPrenomEtudiant(insc.get("prenomEtudiant", String.class));
			inscModel.setTermine(insc.get("termine", Boolean.class));
			inscModel.setAcquis(insc.get("acquis", Boolean.class));

			inscriptionsModel.add(inscModel);

		}

		return inscriptionsModel;

	}

	public List<EtudiantModel> getEtudiantsDispo(Long idSession) {

		List<EtudiantModel> etudiantsModel = new ArrayList<>();
		List<Utilisateur> utilisateurs = repSession.getEtudiantsDispo(idSession);

		for (Utilisateur uti : utilisateurs) {
			Etudiant etu = uti.getProfilEtudiant();
			EtudiantModel etuM = new EtudiantModel();
			etuM.setId(etu.getId());
			etuM.setNom(etu.getUtilisateur().getNom());
			etuM.setPrenom(etu.getUtilisateur().getPrenom());
			etuM.setNumero(etu.getNumero());
			etudiantsModel.add(etuM);
		}

		return etudiantsModel;

	}

	@Transactional
	public void inscrireEtudiant(Long idSession, Long idEtudiant) {

		Etudiant etudiant = repEtudiant.findOne(idEtudiant);
		Session session = repSession.findOne(idSession);

		if (etudiant != null && session != null) {
			InscriptionSession inscS = new InscriptionSession(session, etudiant);
			repSession.save(session);
		} else {
			throw new IllegalArgumentException("Etudiant ou session inconnue.");
		}

	}

	@Transactional
	public void desinscrireEtudiant(Long idSession, Long idInscriptionSession) {

		InscriptionSession inscriptionSession = repInscriptionSession.findOne(idInscriptionSession);
		Session session = repSession.findOne(idSession);

		if (inscriptionSession != null && session != null) {
			session.retirerInscriptionSession(inscriptionSession);
			repSession.save(session);
		} else {
			throw new IllegalArgumentException("valeurs inconnues en base de donnée");
		}

	}

	public List<InscriptionModuleModel> getInscriptionsModules(Long idInscriptionSession) {
		
		List<InscriptionModuleModel> inscriptionsModuleModel = new ArrayList<>();
		List<InscriptionModule> inscriptionsModule = repInscriptionSession.getInscriptionsModule(idInscriptionSession);
		
		for (InscriptionModule inscM : inscriptionsModule) {
			
			InscriptionModuleModel inscMM = new InscriptionModuleModel();
			Module mod = inscM.getModule();
			inscMM.setCodeModule(mod.getCode());
			inscMM.setCoefficient(mod.getCoefficient());
			inscMM.setNomModule(mod.getNom());
			inscMM.setNoteSeuil(mod.getNoteSeuil());
			inscMM.setRattrapable(mod.isRattrapable());
			inscMM.setId(inscM.getId());
			inscMM.setNote(inscM.getNote());
			inscMM.setAcquis(inscM.isAcquis());
			inscMM.setTermine(inscM.isTermine());
			
			List<InscriptionDevoirModel> inscriptionsDevoirModel = new ArrayList<>();
			for (InscriptionDevoir inscD : inscM.getInscriptionsDevoir()) {
				
				InscriptionDevoirModel inscDM = new InscriptionDevoirModel();
				Devoir devoir = inscD.getDevoir();
				inscDM.setCoefficient(devoir.getCoefficient());
				inscDM.setNomDevoir(devoir.getNom());
				inscDM.setId(inscD.getId());
				inscDM.setNote(inscD.getNote());
				
				inscriptionsDevoirModel.add(inscDM);
			}
			
			// réordonner la liste des devoirs dans l'ordre des noms de devoir
			Comparator<InscriptionDevoirModel> byNomComparator = 
					(InscriptionDevoirModel id1, InscriptionDevoirModel id2) -> id1.getNomDevoir().compareTo(id2.getNomDevoir());
			inscriptionsDevoirModel.sort(byNomComparator);
			
			inscMM.setInscriptionsDevoirs(inscriptionsDevoirModel);
			
			inscriptionsModuleModel.add(inscMM);
		}
			 	
		return inscriptionsModuleModel;
		
	}

	public InscriptionSessionModel getInscriptionSession(Long idInscriptionSession) {
		
		InscriptionSession inscS = repInscriptionSession.getInscriptionSession(idInscriptionSession);
		InscriptionSessionModel inscSM = new InscriptionSessionModel();
		
		inscSM.setId(inscS.getId());
		inscSM.setNote(inscS.getNote());
		inscSM.setAcquis(inscS.isAcquis());
		inscSM.setTermine(inscS.isTermine());
		
		Utilisateur uti = inscS.getEtudiant().getUtilisateur();
		inscSM.setNomEtudiant(uti.getNom());
		inscSM.setPrenomEtudiant(uti.getPrenom());
						
		return inscSM;
		
	}
	
	// retourner les modules auquel un étudiant peut être inscrit dans une session.
	public List<ModuleModel> getModuleDispo(Long idInscriptionSession) {
		
		List<ModuleModel> modulesModel = new ArrayList<>();
		List<Module> modules = repSession.getModulesDispo(idInscriptionSession);
		
		for (Module mod : modules) {
			
			ModuleModel modM = new ModuleModel(mod.getId(), mod.getCode());
			modM.setNom(mod.getNom());
			modulesModel.add(modM);
		}
		
		return modulesModel;
	}

	@Transactional
	public void inscrireModule(Long idInscriptionSession, Long idModule) {
		
		InscriptionSession inscS = repInscriptionSession.findOne(idInscriptionSession);
		Module mod = repModule.findOne(idModule);
		InscriptionModule inscM = new InscriptionModule(inscS, mod);
		
		repInscriptionSession.save(inscS);
		
	}
	
	@Transactional
	public void desinscrireModule(Long idInscriptionSession, Long idInscriptionModule) {
		
		InscriptionSession inscS = repInscriptionSession.findOne(idInscriptionSession);
		InscriptionModule inscM = repInscriptionModule.findOne(idInscriptionModule);
		inscS.retirerInscriptionModule(inscM);
		
		repInscriptionSession.save(inscS);
		
	}

	public List<InscriptionDevoirModel> getInscriptionsDevoir(Long idSession, Long idDevoir) {
		
		List<InscriptionDevoirModel> listInscDM = new ArrayList<>();
		List<Tuple> listInscD = repSession.getInscriptionsDevoir(idSession, idDevoir);
		
		for (Tuple tInscD : listInscD) {
			
			InscriptionDevoirModel inscDM = new InscriptionDevoirModel();
			inscDM.setCoefficient(tInscD.get("coefficient", Integer.class));
			inscDM.setId(tInscD.get("id", Long.class));
			inscDM.setNomDevoir(tInscD.get("nomDevoir", String.class));
			inscDM.setNomEtudiant(tInscD.get("nomEtudiant", String.class));
			inscDM.setNote(tInscD.get("note", Double.class));
			inscDM.setNumeroEtudiant(tInscD.get("numeroEtudiant", String.class));
			inscDM.setPrenomEtudiant(tInscD.get("prenomEtudiant", String.class));
			
			listInscDM.add(inscDM);
		}
		
		return listInscDM;
		
	}

	@Transactional
	public void noterDevoir(NoteDevoirModel noteDevoir) {
		
		InscriptionDevoir inscD = repInscriptionDevoir.findOne(noteDevoir.getIdInscriptionDevoir());
		inscD.setNote(noteDevoir.getNote());
		repInscriptionDevoir.save(inscD);
		
	}
	
	@Transactional
	public void annulerNoteDevoir(NoteDevoirModel noteDevoir) {
		
		InscriptionDevoir inscD = repInscriptionDevoir.findOne(noteDevoir.getIdInscriptionDevoir());
		inscD.annulerNote();
		repInscriptionDevoir.save(inscD);
		
	}
	
	

}
