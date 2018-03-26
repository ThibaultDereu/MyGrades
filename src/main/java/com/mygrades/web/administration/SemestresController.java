package com.mygrades.web.administration;

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

import com.mygrades.services.FilieresService;

/**
 * Ce contrôleur permet de gérer les semestres rattachés à une filière.
 */
@Controller
public class SemestresController {

	@Autowired
	FilieresService filieresService;

	/**
	 * Liste des semestres d'une filière.
	 */
	@GetMapping(value = "/filieres/{idFiliere}/semestres")
	public String listerSemestres(@PathVariable String idFiliere, Model model) {
		Long longIdFiliere = Long.parseLong(idFiliere);
		FiliereModel filiere = filieresService.getFiliere(longIdFiliere);
		List<SemestreModel> semestresModel = filieresService.getSemestres(longIdFiliere);
		model.addAttribute("filiere", filiere);
		model.addAttribute("semestres", semestresModel);
		return "semestres";
	}

	/**
	 * Formulaire de création d'un semestre.
	 */
	@GetMapping(value = "/filieres/{idFiliere}/semestres/formulaire_semestre")
	public String ajouterSemestre(Model model) {
		model.addAttribute("semestre", new SemestreModel());
		return "fragments/formulaire_semestre";
	}

	/**
	 * Validation de la création d'un semestre.
	 */
	@PostMapping(value = "/filieres/{idFiliere}/semestres/creer_semestre")
	public String validerAjoutSemestre(@PathVariable String idFiliere,
			@Valid @ModelAttribute("semestre") SemestreModel semestreModel, BindingResult bindingResult,
			HttpServletResponse response) {

		Long longIdFiliere = Long.parseLong(idFiliere);

		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			filieresService.creerSemestre(longIdFiliere, semestreModel);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("appMessage", String.format("Le semestre %s a été créé.", semestreModel.getNom()));
		}

		return "fragments/formulaire_semestre";
	}

	/**
	 * Formulaire de modification/suppression de semestre
	 */
	@GetMapping(value = "/filieres/{idFiliere}/semestres/formulaire_semestre/{idSemestre}")
	public String modifierSemestre(@PathVariable("idSemestre") String idSemestre, Model model) {

		Long longIdSemestre = Long.parseLong(idSemestre);
		SemestreModel semestreModel = filieresService.getSemestre(longIdSemestre);
		model.addAttribute("semestre", semestreModel);

		return "fragments/formulaire_semestre";
	}

	/**
	 * Valider la modification d'un semestre.
	 */
	@PostMapping(value = "/filieres/{idFiliere}/semestres/modifier_semestre")
	public String validerModifSemestre(@Valid @ModelAttribute("semestre") SemestreModel semestreModel,
			BindingResult bindingResult, HttpServletResponse response) {

		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			filieresService.modifierSemestre(semestreModel);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("appMessage", String.format("Le semestre %s a été modifié.", semestreModel.getNom()));
		}

		return "fragments/formulaire_semestre";
	}

	/**
	 * Valider la suppression d'un semestre.
	 */
	@PostMapping(value = "/filieres/{idFiliere}/semestres/supprimer_semestre")
	public String supprimerSemestre(@ModelAttribute("semestre") SemestreModel semestreModel,
			HttpServletResponse response) {

		String message;

		try {
			filieresService.supprimerSemestre(semestreModel);
			message = String.format("Semestre %s supprimé.", semestreModel.getNom());
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (DataIntegrityViolationException e) {
			message = "Suppression impossible : semestre utilisé.";
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		response.setHeader("appMessage", message);
		return "fragments/formulaire_semestre";
	}

}
