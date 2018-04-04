package com.mygrades.web.administration;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mygrades.services.FilieresService;


/**
 * Cette classe contrôleur permet aux administrateurs de gérer la partie "filières".
 */

//@PreAuthorize("hasRole('ROLE_ADMIN')")
@Controller
public class FilieresController {

	@Autowired
	FilieresService filieresService;
	
	/**
	 * Attribut optionMenu pour connaître l'option menu à faire apparaître en actif.
	 */
	@ModelAttribute
	public void ajouterOptionMenu(Model model) {
		model.addAttribute("optionMenu", "administration");
		model.addAttribute("optionSousMenu", "filieres");
	}
	
	/**
	 * Liste des filières.
	 */
	@GetMapping("/filieres")
	public String listerFilieres(Model model, @ModelAttribute("filiereModel") FiliereModel filiereModel) {
		List<FiliereModel> listFilieres = filieresService.getListFilieres();
		model.addAttribute("filieres", listFilieres);
		return "filieres";
	}
	
	
	/**
	 * Formulaire de création d'une nouvelle filière.
	 */
	@GetMapping(value = "/filieres/formulaire_filiere")
	public String ajouterFiliere(@ModelAttribute("filiereModel") FiliereModel filiereModel) {
		// le paramètre filiereModel est automatiquement ajouté au modèle, avec valeurs
		// null;
		return "fragments/formulaire_filiere";
	}
	
	
	/**
	 * Validation de création d'une nouvelle filière.
	 */
	@PostMapping(value = "/filieres/creer_filiere")
	public String validerAjoutFiliere(@Valid FiliereModel filiereModel, BindingResult bindingResult,
			HttpServletResponse response) {
		
		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			filieresService.creerFiliere(filiereModel);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("appMessage", String.format("La filière %s a été créée.", filiereModel.getNom()));
		}
		
		return "fragments/formulaire_filiere";
	}

	
	/**
	 * Formulaire de modification ou ajout d'une filière existante. (même vue que
	 * pour la création)
	 */
	@GetMapping("/filieres/formulaire_filiere/{filiereId}")
	public String modifierFiliere(@PathVariable String filiereId, Model model) {
		FiliereModel filiereModel = filieresService.getFiliere(Long.parseLong(filiereId));
		model.addAttribute("filiereModel", filiereModel);
		return "fragments/formulaire_filiere";
	}

	
	/**
	 * Validation de modification d'une filière existante.
	 */
	@PostMapping(value = "/filieres/modifier_filiere")
	public String validerModificationFiliere(@Valid FiliereModel filiereModel, BindingResult bindingResult,
			HttpServletResponse response) {
		
		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			filieresService.modifierFiliere(filiereModel);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("appMessage", String.format("La filière %s a été modifiée.", filiereModel.getNom()));
		}
		
		return "fragments/formulaire_filiere";
	}
	
	
	/**
	 * Suppression d'une filière existante.
	 */
	@PostMapping(value = "/filieres/supprimer_filiere")
	public String supprimerFiliere(FiliereModel filiereModel, HttpServletResponse response) {
		String message;
		try {
			filieresService.supprimerFiliere(filiereModel);
			message = String.format("Filière %s supprimée.", filiereModel.getNom());
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (DataIntegrityViolationException e) {
			message = "Suppression impossible : filière utilisée.";
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		response.setHeader("appMessage", message);
		return "fragments/formulaire_filiere";
	}

}
