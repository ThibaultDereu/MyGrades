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
import com.mygrades.services.UtilisateursService;

@Controller
public class UtilisateursController {

	@Autowired
	UtilisateursService utilisateursService;
	
	@Autowired
	FilieresService filieresService;

	@GetMapping("/utilisateurs")
	public String listeUtilisateurs(Model model) {

		List<UtilisateurModel> utilisateurs = utilisateursService.getListeUtilisateurs();
		model.addAttribute("utilisateurs", utilisateurs);
		return "utilisateurs";

	}

	/*
	 * ------------------------- Gestion des utilisateurs -------------------------
	 */

	/**
	 * affichage du formulaire utilisateur vide
	 */
	@GetMapping("/utilisateurs/formulaire_utilisateur")
	public String nouvelUtilisateur(@ModelAttribute("utilisateur") UtilisateurModel utilisateurModel) {

		return "fragments/formulaire_utilisateur";

	}

	/**
	 * affichage du formulaire utilisateur pour utilisateur existant
	 */
	@GetMapping("/utilisateurs/formulaire_utilisateur/{idUtilisateur}")
	public String afficherUtilisateur(Model model, @PathVariable("idUtilisateur") String strIdUtilisateur) {

		Long idUtilisateur = Long.parseLong(strIdUtilisateur);
		UtilisateurModel um = utilisateursService.getUtilisateur(idUtilisateur);
		model.addAttribute("utilisateur", um);

		return "fragments/formulaire_utilisateur";
	}

	/**
	 * validation de la création d'un utilisateur
	 */
	@PostMapping("/utilisateurs/creer_utilisateur")
	public String validerCreationUtilisateur(@Valid @ModelAttribute("utilisateur") UtilisateurModel utilisateurModel,
			BindingResult bindingResult, HttpServletResponse response) {

		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			try {
				utilisateursService.creerUtilisateur(utilisateurModel);
				response.setStatus(HttpServletResponse.SC_OK);
				response.setHeader("appMessage", String.format("L'utilisateur %s %s a été créé.",
						utilisateurModel.getPrenom(), utilisateurModel.getNom()));
			} catch (DataIntegrityViolationException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setHeader("appMessage",
						"Création impossible. Vérifiez que l'adresse e-mail n'existe pas déjà.");
			}

		}

		return "fragments/formulaire_utilisateur";

	}

	/**
	 * validation de la modification d'un utilisateur
	 */
	@PostMapping("/utilisateurs/modifier_utilisateur")
	public String validerModificationUtilisateur(
			@Valid @ModelAttribute("utilisateur") UtilisateurModel utilisateurModel, BindingResult bindingResult,
			HttpServletResponse response) {

		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			try {
				utilisateursService.modifierUtilisateur(utilisateurModel);
				response.setStatus(HttpServletResponse.SC_OK);
				response.setHeader("appMessage", String.format("L'utilisateur %s %s a été modifié.",
						utilisateurModel.getPrenom(), utilisateurModel.getNom()));
			} catch (DataIntegrityViolationException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setHeader("appMessage",
						"Modification impossible. Vérifiez que l'adresse e-mail n'existe pas déjà.");
			}
		}

		return "fragments/formulaire_utilisateur";

	}

	/**
	 * validation de suppression d'un utilisateur
	 */
	@PostMapping("/utilisateurs/supprimer_utilisateur")
	public String validerSuppressionUtilisateur(@ModelAttribute("utilisateur") UtilisateurModel utilisateurModel,
			HttpServletResponse response) {

		String message;

		try {
			utilisateursService.supprimerUtilisateur(utilisateurModel.getId());
			message = String.format("Utilisateur %s supprimé.", utilisateurModel.getPrenom(),
					utilisateurModel.getNom().toUpperCase());
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (DataIntegrityViolationException e) {
			message = String.format(
					"L'utilisateur %s %s ne peut pas être supprimé. Vérifiez qu'il n'est pas inscrit dans une session.",
					utilisateurModel.getPrenom(), utilisateurModel.getNom().toUpperCase());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		response.setHeader("appMessage", message);
		return "fragments/formulaire_utilisateur";

	}

	/*
	 * ------------------------- Gestion des étudiants -------------------------
	 */
	@GetMapping("/utilisateurs/formulaire_etudiant/{idUtilisateur}")
	public String afficherEtudiant(@PathVariable("idUtilisateur") String strIdUtilisateur, Model model) {

		Long idUtilisateur = Long.parseLong(strIdUtilisateur);
		EtudiantModel etudiantModel = utilisateursService.getProfilEtudiant(idUtilisateur);
		model.addAttribute("etudiant", etudiantModel);

		return "fragments/formulaire_etudiant";

	}

	@PostMapping("/utilisateurs/creer_etudiant")
	public String validerCreationEtudiant(@Valid @ModelAttribute("etudiant") EtudiantModel etudiantModel,
			BindingResult bindingResult, HttpServletResponse response) {

		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			try {
				utilisateursService.creerProfilEtudiant(etudiantModel);
				response.setStatus(HttpServletResponse.SC_OK);
				response.setHeader("appMessage", String.format("Le profil étudiant de l'utilisateur %s %s a été créé.",
						etudiantModel.getPrenom(), etudiantModel.getNom()));
			} catch (DataIntegrityViolationException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setHeader("appMessage", "Création impossible. Vérifiez que le numéro n'existe pas déjà.");
			}
		}
		return "fragments/formulaire_etudiant";
	}

	@PostMapping("/utilisateurs/modifier_etudiant")
	public String validerModificationEtudiant(@Valid @ModelAttribute("etudiant") EtudiantModel etudiantModel,
			BindingResult bindingResult, HttpServletResponse response) {

		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			try {
				utilisateursService.modifierEtudiant(etudiantModel);
				response.setStatus(HttpServletResponse.SC_OK);
				response.setHeader("appMessage",
						String.format("Le profil étudiant de l'utilisateur %s %s a été modifié.",
								etudiantModel.getPrenom(), etudiantModel.getNom()));
			} catch (DataIntegrityViolationException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setHeader("appMessage",
						"Modification impossible. Vérifiez que le numéro d'étudiant n'existe pas déjà.");
			}
		}

		return "fragments/formulaire_etudiant";

	}

	@PostMapping("/utilisateurs/supprimer_etudiant")
	public String validerSuppressionEtudiant(@ModelAttribute("etudiant") EtudiantModel etudiantModel,
			HttpServletResponse response) {

		String message;

		try {
			utilisateursService.supprimerEtudiant(etudiantModel.getIdUtilisateur());
			message = String.format("Le profil étudiant de l'utilisateur %s %s a été supprimé.",
					etudiantModel.getPrenom(), etudiantModel.getNom().toUpperCase());
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (DataIntegrityViolationException e) {
			message = String.format(
					"Le profil étudiant de l'utilisateur %s %s ne peut pas être supprimé. Vérifiez qu'il n'est pas inscrit dans une session.",
					etudiantModel.getPrenom(), etudiantModel.getNom().toUpperCase());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		response.setHeader("appMessage", message);
		return "fragments/formulaire_etudiant";
	}

	/*
	 * ------------------------- Gestion des enseignants -------------------------
	 */
	@GetMapping("/utilisateurs/formulaire_enseignant/{idUtilisateur}")
	public String afficherEnseignant(@PathVariable("idUtilisateur") String strIdUtilisateur, Model model) {

		Long idUtilisateur = Long.parseLong(strIdUtilisateur);
		EnseignantModel enseignantModel = utilisateursService.getProfilEnseignant(idUtilisateur);
		model.addAttribute("enseignant", enseignantModel);
		
		List<ModuleModelSimple> modulesModel = filieresService.getAllModulesSimple();
		model.addAttribute("modulesDispo", modulesModel);

		return "fragments/formulaire_enseignant";

	}

	@PostMapping("/utilisateurs/creer_enseignant")
	public String validerCreationEnseignant(@Valid @ModelAttribute("enseignant") EnseignantModel enseignantModel,
			BindingResult bindingResult, HttpServletResponse response) {

		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			try {
				utilisateursService.creerProfilEnseignant(enseignantModel);
				response.setStatus(HttpServletResponse.SC_OK);
				response.setHeader("appMessage", String.format("Le profil enseignant de l'utilisateur %s %s a été créé.",
						enseignantModel.getPrenom(), enseignantModel.getNom()));
			} catch (DataIntegrityViolationException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setHeader("appMessage", "Création impossible. Vérifiez que les données saisies sont valides.");
			}
		}
		return "fragments/formulaire_enseignant";
	}

	@PostMapping("/utilisateurs/supprimer_enseignant")
	public String validerSuppressionEnseignant(@ModelAttribute("enseignant") EnseignantModel enseignantModel,
			HttpServletResponse response) {

		String message;

		try {
			utilisateursService.supprimerEnseignant(enseignantModel.getIdUtilisateur());
			message = String.format("Le profil enseignant de l'utilisateur %s %s a été supprimé.",
					enseignantModel.getPrenom(), enseignantModel.getNom().toUpperCase());
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (DataIntegrityViolationException e) {
			message = String.format(
					"Le profil enseignant de l'utilisateur %s %s ne peut pas être supprimé. Vérifiez qu'il n'est pas associé à un module.",
					enseignantModel.getPrenom(), enseignantModel.getNom().toUpperCase());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		response.setHeader("appMessage", message);
		return "fragments/formulaire_enseignant";
	}
	
}
