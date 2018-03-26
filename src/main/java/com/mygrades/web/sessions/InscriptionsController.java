package com.mygrades.web.sessions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mygrades.services.SessionsService;
import com.mygrades.web.administration.EtudiantModel;
import com.mygrades.web.administration.ModuleModel;

@Controller
public class InscriptionsController {

	@Autowired
	SessionsService sessionsService;

	/**
	 * Gestion des étudiants inscrits dans une session.
	 */
	@GetMapping("/filieres_sessions/{idFiliere}/sessions/{idSession}/etudiants")
	public String getEtudiantsInscrits(@PathVariable("idSession") String strIdSession,
			@PathVariable("idFiliere") String strIdFiliere, Model model) {

		Long idFiliere = Long.parseLong(strIdFiliere);
		Long idSession = Long.parseLong(strIdSession);
		SessionModel session = sessionsService.getSession(idSession);
		List<InscriptionSessionModel> inscriptions = sessionsService.getInscriptionsSession(idSession);
		model.addAttribute("idFiliere", idFiliere);
		model.addAttribute("sessionModel", session);
		model.addAttribute("inscriptions", inscriptions);

		List<EtudiantModel> etudiants = sessionsService.getEtudiantsDispo(idSession);
		model.addAttribute("etudiantsDispo", etudiants);

		return "/etudiants_inscrits";

	}

	/**
	 * Ajouter un étudiant dans une session.
	 */
	@PostMapping(value = "filieres_sessions/{idFiliere}/sessions/{idSession}/etudiants", params = "action=ajouter")
	public String ajouterInscriptionSession(@PathVariable("idFiliere") String strIdFiliere,
			@PathVariable("idSession") String strIdSession, Model model, RedirectAttributes redAttr,
			@RequestParam(value = "select-etudiants", required = false) String strIdEtudiant) {

		if (strIdEtudiant != null) {
			Long idEtudiant = Long.parseLong(strIdEtudiant);
			Long idSession = Long.parseLong(strIdSession);
			try {
				sessionsService.inscrireEtudiant(idSession, idEtudiant);
				redAttr.addFlashAttribute("message", String.format("Etudiant inscrit."));
			} catch (IllegalStateException e) {
				redAttr.addFlashAttribute("message", e.getMessage());
			}
			
		}
		return "redirect:/filieres_sessions/{idFiliere}/sessions/{idSession}/etudiants";

	}

	/**
	 * Retirer un étudiant d'une session.
	 */
	@PostMapping(value = "filieres_sessions/{idFiliere}/sessions/{idSession}/etudiants", params = "action=retirer")
	public String retirerInscriptionSession(@PathVariable("idFiliere") String strIdFiliere,
			@PathVariable("idSession") String strIdSession,
			@RequestParam("idInscriptionSession") String strIdInscriptionSession, Model model,
			RedirectAttributes redAttr) {

		if (strIdInscriptionSession != null) {
			Long idInscriptionSession = Long.parseLong(strIdInscriptionSession);
			Long idSession = Long.parseLong(strIdSession);
			try {
				sessionsService.desinscrireEtudiant(idSession, idInscriptionSession);
				redAttr.addFlashAttribute("message", String.format("Etudiant retiré de la session."));
			} catch (IllegalArgumentException e) {
				redAttr.addFlashAttribute("message", e.getMessage());
			}
			
		}
		return "redirect:/filieres_sessions/{idFiliere}/sessions/{idSession}/etudiants";
	}

	/**
	 * Gestion des modules auxquels un étudiant est inscrit dans une session.
	 */
	@GetMapping("/filieres_sessions/{idFiliere}/sessions/{idSession}/etudiants/{idInscriptionSession}/modules")
	public String getEtudiantsInscrits(@PathVariable("idFiliere") String strIdFiliere,
			@PathVariable("idSession") String strIdSession,
			@PathVariable("idInscriptionSession") String strIdInscriptionSession, Model model) {

		Long idFiliere = Long.parseLong(strIdFiliere);
		Long idSession = Long.parseLong(strIdSession);
		Long idInscriptionSession = Long.parseLong(strIdInscriptionSession);
		SessionModel sessionModel = sessionsService.getSession(idSession);
		InscriptionSessionModel inscriptionSessionModel = sessionsService.getInscriptionSession(idInscriptionSession);
		List<InscriptionModuleModel> inscriptionsModules = sessionsService.getInscriptionsModules(idInscriptionSession);

		model.addAttribute("idFiliere", idFiliere);
		model.addAttribute("sessionModel", sessionModel);
		model.addAttribute("inscriptionSession", inscriptionSessionModel);
		model.addAttribute("inscriptionsModule", inscriptionsModules);

		List<ModuleModel> modulesDispo = sessionsService.getModuleDispo(idInscriptionSession);
		model.addAttribute("modulesDispo", modulesDispo);

		return "/inscriptions_modules";

	}

	// filieres_sessions/1/sessions/1/etudiants/23/modules
	/**
	 * Ajouter un module dans une inscription session
	 */
	@PostMapping(value = "filieres_sessions/{idFiliere}/sessions/{idSession}/etudiants/{idInscriptionSession}/modules", params = "action=ajouter")
	public String validerInscriptionModule(@PathVariable("idInscriptionSession") String strIdInscriptionSession,
			Model model, RedirectAttributes redAttr,
			@RequestParam(value = "select-modules", required = false) String strIdModule) {

		if (strIdInscriptionSession != null) {
			Long idInscriptionSession = Long.parseLong(strIdInscriptionSession);
			Long idModule = Long.parseLong(strIdModule);
			try {
				sessionsService.inscrireModule(idInscriptionSession, idModule);
				redAttr.addFlashAttribute("message", String.format("Etudiant inscrit au module."));
			} catch (IllegalStateException e) {
				redAttr.addFlashAttribute("message", e.getMessage());
			}
		}
		return "redirect:/filieres_sessions/{idFiliere}/sessions/{idSession}/etudiants/{idInscriptionSession}/modules";

	}

	/**
	 * Retirer un module d'une inscription session.
	 */
	@PostMapping(value = "filieres_sessions/{idFiliere}/sessions/{idSession}/etudiants/{idInscriptionSession}/modules", params = "action=retirer")
	public String retirerInscriptionModule(@PathVariable("idInscriptionSession") String strIdInscriptionSession,
			Model model, RedirectAttributes redAttr,
			@RequestParam(value = "idInscriptionModule") String strIdInscriptionModule) {

		Long idInscriptionSession = Long.parseLong(strIdInscriptionSession);
		Long idInscriptionModule = Long.parseLong(strIdInscriptionModule);
		try {
			sessionsService.desinscrireModule(idInscriptionSession, idInscriptionModule);
			redAttr.addFlashAttribute("message", String.format("Module retiré."));
		} catch (IllegalArgumentException e) {
			redAttr.addFlashAttribute("message", e.getMessage());
		}
		
		
		return "redirect:/filieres_sessions/{idFiliere}/sessions/{idSession}/etudiants/{idInscriptionSession}/modules";
	}

}
