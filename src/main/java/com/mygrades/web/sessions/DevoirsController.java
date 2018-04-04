package com.mygrades.web.sessions;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mygrades.services.FilieresService;
import com.mygrades.services.SessionsService;

@Controller
public class DevoirsController {

	@Autowired
	SessionsService sessionsService;

	@Autowired
	FilieresService filieresService;
	
	/**
	 * Attribut optionMenu pour connaître l'option menu à faire apparaître en actif.
	 */
	@ModelAttribute
	public void ajouterOptionMenu(Model model) {
		model.addAttribute("optionMenu", "sessions");
	}
	
	@GetMapping("/filieres_sessions/{idFiliere}/sessions/{idSession}/devoirs")
	public String afficherDevoirs(@PathVariable("idFiliere") String strIdFiliere,
			@PathVariable("idSession") String strIdSession, Model model) {

		Long idFiliere = Long.parseLong(strIdFiliere);
		Long idSession = Long.parseLong(strIdSession);
		SessionModel session = sessionsService.getSession(idSession);
		List<ModuleAvecDevoirsModel> modules = sessionsService.getModulesAvecDevoirs(idSession);
		model.addAttribute("idFiliere", idFiliere);
		model.addAttribute("sessionModel", session);
		model.addAttribute("modules", modules);

		return "devoirs";
	}

	@GetMapping("/filieres_sessions/{idFiliere}/sessions/{idSession}/devoirs/{idModule}/formulaire_devoir")
	public String afficherFormulaireNouveauDevoir(@PathVariable("idSession") String strIdSession,
			@PathVariable("idModule") String strIdModule, Model model) {

		Long idModule = Long.parseLong(strIdModule);
		DevoirModel devoir = new DevoirModel();
		sessionsService.injecteModuleDansDevoir(devoir, idModule);

		model.addAttribute("devoir", devoir);

		return "fragments/formulaire_devoir";

	}

	@PostMapping("/filieres_sessions/{idFiliere}/sessions/{idSession}/devoirs/creer_devoir")
	public String validerCreationDevoir(@PathVariable("idSession") String strIdSession,
			@Valid @ModelAttribute("devoir") DevoirModel devoir, BindingResult bindingResult,
			HttpServletResponse response) {

		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			Long idSession = Long.parseLong(strIdSession);
			
			try {
				sessionsService.creerDevoir(idSession, devoir);
				response.setStatus(HttpServletResponse.SC_OK);
				response.setHeader("appMessage", String.format("Le devoir %s a été créé dans le module %s.",
						devoir.getNom(), devoir.getNomModule()));
			} catch (IllegalArgumentException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setHeader("appMessage", e.getMessage());
			}
			
		}

		return "fragments/formulaire_devoir";
	}

	@GetMapping("/filieres_sessions/{idFiliere}/sessions/{idSession}/devoirs/formulaire_devoir/{idDevoir}")
	public String afficherFormulaireDevoirExistant(@PathVariable("idDevoir") String strIdDevoir, Model model) {

		Long idDevoir = Long.parseLong(strIdDevoir);
		DevoirModel devoirModel = sessionsService.getDevoir(idDevoir);

		model.addAttribute("devoir", devoirModel);

		return "fragments/formulaire_devoir";

	}

	@PostMapping("/filieres_sessions/{idFiliere}/sessions/{idSession}/devoirs/modifier_devoir")
	public String validerModificationDevoir(@PathVariable("idSession") String strIdSession,
			@Valid @ModelAttribute("devoir") DevoirModel devoir, BindingResult bindingResult,
			HttpServletResponse response) {

		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			Long idSession = Long.parseLong(strIdSession);
			sessionsService.modifierDevoir(idSession, devoir);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("appMessage",
					String.format("Le devoir %s du module %s a été modifié.", devoir.getNom(), devoir.getNomModule()));
		}

		return "fragments/formulaire_devoir";
	}

	@PostMapping("/filieres_sessions/{idFiliere}/sessions/{idSession}/devoirs/supprimer_devoir")
	public String validerSuppressionDevoir(@PathVariable("idSession") String strIdSession,
			@ModelAttribute("devoir") DevoirModel devoir, HttpServletResponse response) {

		try {

			Long idSession = Long.parseLong(strIdSession);
			sessionsService.supprimerDevoir(idSession, devoir);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("appMessage",
					String.format("Le devoir %s du module %s a été modifié.", devoir.getNom(), devoir.getNomModule()));

		} catch (DataIntegrityViolationException e) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setHeader("appMessage", String.format("Suppression impossible du devoir %s du module %s",
					devoir.getNom(), devoir.getNomModule()));

		}

		return "fragments/formulaire_devoir";

	}

	@GetMapping("/filieres_sessions/{idFiliere}/sessions/{idSession}/devoirs/{idDevoir}/notes")
	public String afficherNotesDevoirs(@PathVariable("idFiliere") String strIdFiliere,
			@PathVariable("idSession") String strIdSession, @PathVariable("idDevoir") String strIdDevoir, Model model) {

		Long idFiliere = Long.parseLong(strIdFiliere);
		Long idSession = Long.parseLong(strIdSession);
		Long idDevoir = Long.parseLong(strIdDevoir);

		SessionModel session = sessionsService.getSession(idSession);
		DevoirModel devoir = sessionsService.getDevoir(idDevoir);
		List<InscriptionDevoirModel> inscriptionsDevoir = sessionsService.getInscriptionsDevoir(idSession, idDevoir);

		model.addAttribute("idFiliere", idFiliere);
		model.addAttribute("sessionModel", session);
		model.addAttribute("devoir", devoir);
		model.addAttribute("inscriptionsDevoir", inscriptionsDevoir);

		return "notes_devoir";

	}

	@ResponseBody
	@PostMapping("/filieres_sessions/{idFiliere}/sessions/{idSession}/devoirs/{idDevoirs}/notes/noter_devoir")
	public NoteDevoirModel validerNoteDevoir(@Valid @ModelAttribute("noteDevoir") NoteDevoirModel noteDevoir,
			BindingResult bindingResult, HttpServletResponse response) {

		if (!bindingResult.hasErrors()) {
			sessionsService.noterDevoir(noteDevoir);
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		return noteDevoir;

	}

	@ResponseBody
	@PostMapping("/filieres_sessions/{idFiliere}/sessions/{idSession}/devoirs/{idDevoirs}/notes/annuler_note_devoir")
	public NoteDevoirModel annulerNoteDevoir(@ModelAttribute("noteDevoir") NoteDevoirModel noteDevoir,
			HttpServletResponse response) {
		
		try {
			sessionsService.annulerNoteDevoir(noteDevoir);
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
		} catch (IllegalStateException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		return noteDevoir;
	}
}
