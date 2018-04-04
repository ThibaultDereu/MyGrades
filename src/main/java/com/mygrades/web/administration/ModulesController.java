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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mygrades.services.FilieresService;
import com.mygrades.services.UtilisateursService;

@Controller
public class ModulesController {

	@Autowired
	FilieresService filieresService;

	@Autowired
	UtilisateursService utilisateursService;

	/**
	 * Attribut optionMenu pour connaître l'option menu à faire apparaître en actif.
	 */
	@ModelAttribute
	public void ajouterOptionMenu(Model model) {
		model.addAttribute("optionMenu", "administration");
		model.addAttribute("optionSousMenu", "modules");
	}

	/**
	 * Liste de tous les modules existant.
	 */
	@GetMapping(value = "/modules")
	public String listeModules(Model model) {

		List<ModuleModel> modulesModel = filieresService.getAllModules();
		model.addAttribute("modules", modulesModel);

		return "modules";
	}

	/**
	 * Affichage du formulaire de création d'un module.
	 */
	@GetMapping(value = "/modules/formulaire_module")
	public String getFormulaireNouveauModule(@ModelAttribute("module") ModuleModel module, Model model) {

		List<EnseignantModel> enseignantsModel = utilisateursService.getListeEnseignants();
		model.addAttribute("enseignants", enseignantsModel);

		return "fragments/formulaire_module";
	}

	/**
	 * Validation de la création d'un module.
	 */
	@PostMapping(value = "/modules/creer_module")
	public String validerCreationModule(@Valid @ModelAttribute("module") ModuleModel moduleModel,
			BindingResult bindingResult, HttpServletResponse response, Model model) {

		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			try {
				filieresService.creerModule(moduleModel);
				response.setStatus(HttpServletResponse.SC_OK);
				response.setHeader("appMessage", String.format("Le module %s a été créé.", moduleModel.getCode()));
			} catch (DataIntegrityViolationException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setHeader("appMessage", String.format(
						"Création impossible. Vérifiez que le code module %s n'existe pas déjà, et que les données saisies sont correctes.",
						moduleModel.getCode()));
			}
		}
		List<EnseignantModel> enseignantsModel = utilisateursService.getListeEnseignants();
		model.addAttribute("enseignants", enseignantsModel);

		return "fragments/formulaire_module";
	}

	/**
	 * Affichage du formulaire de modification d'un module
	 */
	@GetMapping(value = "/modules/formulaire_module/{idModule}")
	public String getFormulaireModuleExistant(@PathVariable("idModule") String strIdModule, Model model) {
		Long idModule = Long.parseLong(strIdModule);
		ModuleModel moduleModel = filieresService.getModule(idModule);
		model.addAttribute("module", moduleModel);

		List<EnseignantModel> enseignantsModel = utilisateursService.getListeEnseignants();
		model.addAttribute("enseignants", enseignantsModel);

		return "fragments/formulaire_module";
	}

	/**
	 * Validation de la modification d'un module.
	 */
	@PostMapping(value = "/modules/modifier_module")
	public String validerModificationModule(@Valid @ModelAttribute("module") ModuleModel moduleModel,
			BindingResult bindingResult, HttpServletResponse response, Model model) {

		if (bindingResult.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			try {
				filieresService.modifierModule(moduleModel);
				response.setStatus(HttpServletResponse.SC_OK);
				response.setHeader("appMessage", String.format("Le module %s a été modifié.", moduleModel.getCode()));
			} catch (DataIntegrityViolationException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setHeader("appMessage", String.format(
						"Modification impossible. Vérifiez que le code module %s n'existe pas déjà, et que les données saisies sont correctes.",
						moduleModel.getCode()));
			}
		}

		List<EnseignantModel> enseignantsModel = utilisateursService.getListeEnseignants();
		model.addAttribute("enseignants", enseignantsModel);

		return "fragments/formulaire_module";
	}

	/**
	 * Validation de la suppression d'un module.
	 */
	@PostMapping(value = "/modules/supprimer_module")
	public String validerSuppressionModule(@ModelAttribute("module") ModuleModel moduleModel,
			HttpServletResponse response, Model model) {

		try {
			filieresService.supprimerModule(moduleModel);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("appMessage", String.format("Le module %s a été supprimé.", moduleModel.getCode()));
		} catch (DataIntegrityViolationException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setHeader("appMessage", String.format(
					"Suppression impossible. Vérifiez que le module %s n'est pas déjà utilisé dans une filière.",
					moduleModel.getCode()));
			List<EnseignantModel> enseignantsModel = utilisateursService.getListeEnseignants();
			model.addAttribute("enseignants", enseignantsModel);
		}

		return "fragments/formulaire_module";
	}

}
