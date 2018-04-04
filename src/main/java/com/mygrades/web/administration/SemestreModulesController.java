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
public class SemestreModulesController {

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
		model.addAttribute("optionSousMenu", "filieres");
	}

	/**
	 * Lister les modules d'un semestre.
	 */
	@GetMapping(value = "/filieres/{idFiliere}/semestres/{idSemestre}/modules")
	public String listerSemestreModules(@PathVariable("idFiliere") String idFiliere,
			@PathVariable("idSemestre") String idSemestre, Model model) {

		Long longIdFiliere = Long.parseLong(idFiliere);
		Long longIdSemestre = Long.parseLong(idSemestre);

		FiliereModel filiereModel = filieresService.getFiliere(longIdFiliere);
		SemestreModel semestreModel = filieresService.getSemestre(longIdSemestre);
		List<ModuleModel> modulesModel = filieresService.getModules(longIdSemestre);
		List<ModuleModelSimple> modulesDispoModel = filieresService.getModulesDispo(longIdSemestre);

		model.addAttribute("modules", modulesModel);
		model.addAttribute("filiere", filiereModel);
		model.addAttribute("semestre", semestreModel);
		model.addAttribute("modulesDispo", modulesDispoModel);

		return "semestre_modules";
	}

	/**
	 * Ajouter un module dans un semestre.
	 */
	@PostMapping(value = "/filieres/{idFiliere}/semestres/{idSemestre}/modules", params = "action=ajouter")
	public String ajouterModuleSemestre(@PathVariable("idFiliere") String idFiliere,
			@PathVariable("idSemestre") String idSemestre, Model model, RedirectAttributes redAttr,
			@RequestParam(value = "select-modules", required = false) String idModule) {
		if (idModule != null) {
			Long longIdModule = Long.parseLong(idModule);
			Long longIdSemestre = Long.parseLong(idSemestre);
			filieresService.ajouterModuleSemestre(longIdSemestre, longIdModule);
			// on passe un message directement dans model, qui sera redirigé
			redAttr.addFlashAttribute("message", String.format("module ajouté."));
		}
		return "redirect:/filieres/{idFiliere}/semestres/{idSemestre}/modules";
	}

	/**
	 * Retirer un module d'un semestre.
	 */
	@PostMapping(value = "/filieres/{idFiliere}/semestres/{idSemestre}/modules", params = "action=retirer")
	public String retirerModuleSemestre(@PathVariable("idSemestre") String idSemestre, Model model,
			RedirectAttributes redAttr, @RequestParam(value = "module", required = false) String idModule) {
		if (idModule != null) {
			Long longIdModule = Long.parseLong(idModule);
			Long longIdSemestre = Long.parseLong(idSemestre);
			filieresService.retirerModuleSemestre(longIdSemestre, longIdModule);
			// on passe un message directement dans model, qui sera redirigé
			redAttr.addFlashAttribute("message", String.format("module retiré."));
		}
		return "redirect:/filieres/{idFiliere}/semestres/{idSemestre}/modules";
	}

}
