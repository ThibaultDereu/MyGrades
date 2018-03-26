package com.mygrades.web.sessions;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mygrades.services.FilieresService;
import com.mygrades.services.SessionsService;
import com.mygrades.web.administration.FiliereModel;
import com.mygrades.web.administration.SemestreModel;

@Controller
public class SessionsController {

	@Autowired
	SessionsService sessionsService;

	@Autowired
	FilieresService filieresService;

	@GetMapping("/filieres_sessions")
	public String afficherFilieresSessions(Model model) {
		List<FiliereSessionsModel> filieres = sessionsService.getAllFilieres();
		model.addAttribute("filieres", filieres);
		return "filieres_sessions";
	}

	@GetMapping("/filieres_sessions/{idFiliere}/sessions")
	public String afficherSessions(@PathVariable("idFiliere") String strIdFiliere, Model model) {

		Long idFiliere = Long.parseLong(strIdFiliere);

		FiliereModel filiere = filieresService.getFiliere(idFiliere);
		model.addAttribute("filiere", filiere);

		List<SessionModel> sessionsActives = sessionsService.getSessions(idFiliere, true);
		model.addAttribute("sessionsActives", sessionsActives);

		List<SessionModel> sessionsCloturees = sessionsService.getSessions(idFiliere, false);
		model.addAttribute("sessionsCloturees", sessionsCloturees);

		return "sessions";
	}

	@GetMapping("/filieres_sessions/{idFiliere}/sessions/formulaire_session")
	public String getFormulaireNouvelleSession(@PathVariable("idFiliere") String strIdFiliere, Model model) {

		Long idFiliere = Long.parseLong(strIdFiliere);
		List<SemestreModel> semestres = filieresService.getSemestres(idFiliere);
		model.addAttribute("semestres", semestres);

		model.addAttribute("sessionModel", new SessionModel());
		return "fragments/formulaire_session";

	}

	@PostMapping("/filieres_sessions/{idFiliere}/sessions/creer_session")
	public String validerCreationSession(@PathVariable("idFiliere") String strIdFiliere,
			@Valid @ModelAttribute("sessionModel") SessionModel sessionModel, BindingResult bindingResult,
			HttpServletResponse response, Model model) {

		if (bindingResult.hasErrors()) {
			Long idFiliere = Long.parseLong(strIdFiliere);
			List<SemestreModel> semestres = filieresService.getSemestres(idFiliere);
			model.addAttribute("semestres", semestres);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			sessionsService.creerSession(sessionModel);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("appMessage", String.format("La session %s a été créé.", sessionModel.getNom()));
		}

		return "fragments/formulaire_session";
	}

	@GetMapping("/filieres_sessions/{idFiliere}/sessions/formulaire_session/{idSession}")
	public String getFormulaireSessionExistante(@PathVariable("idFiliere") String strIdFiliere,
			@PathVariable("idSession") String strIdSession, Model model) {

		Long idSession = Long.parseLong(strIdSession);
		SessionModel sessionModel = sessionsService.getSession(idSession);
		model.addAttribute("sessionModel", sessionModel);

		return "fragments/formulaire_session";

	}

	@PostMapping("/filieres_sessions/{idFiliere}/sessions/modifier_session")
	public String validerModificationSession(@PathVariable("idFiliere") String strIdFiliere,
			@Valid @ModelAttribute("sessionModel") SessionModel sessionModel, BindingResult bindingResult,
			HttpServletResponse response, Model model) {

		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getFieldErrors());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			sessionsService.modifierSession(sessionModel);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("appMessage", String.format("La session %s a été modifiée.", sessionModel.getNom()));
		}

		return "fragments/formulaire_session";

	}

	@PostMapping(value = "/filieres_sessions/{idFiliere}/sessions", params = "action=supprimer")
	public String validerSuppressionSession(@RequestParam("idSession") String strIdSession, RedirectAttributes redAttr) {

		Long idSession = Long.parseLong(strIdSession);
		String message;
		try {
			sessionsService.supprimerSession(idSession);
			message = "La session a été supprimée.";
		} catch (DataIntegrityViolationException e) {
			message = "La session n'a pas pu être supprimée.";
		}
		// le message est mis dans model, qui sera redirigé et repris directement par le
		// template
		redAttr.addFlashAttribute("message", message);
		return "redirect:/filieres_sessions/{idFiliere}/sessions";

	}

	@PostMapping(value = "/filieres_sessions/{idFiliere}/sessions", params = "action=cloturer")
	public String cloturerSession(@RequestParam("idSession") String strIdSession, RedirectAttributes redAttr) {

		Long idSession = Long.parseLong(strIdSession);
		String message;
		try {
			sessionsService.cloturerSession(idSession);
			message = "La session a été clôturée.";
		} catch (DataIntegrityViolationException e) {
			message = "La session n'a pas pu être clôturée. Vérifiez que tous les devoirs ont été notés.";
		}

		redAttr.addFlashAttribute("message", message);
		return "redirect:/filieres_sessions/{idFiliere}/sessions";

	}
}
