package com.mygrades.web.simulateur;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mygrades.services.SimulateurService;

@Controller
public class simulateurController {

	@Autowired
	SimulateurService simulateurService;

	/**
	 * Attribut optionMenu pour connaître l'option menu à faire apparaître en actif.
	 */
	@ModelAttribute
	public void ajouterOptionMenu(Model model) {
		model.addAttribute("optionMenu", "simulateur");
	}
	
		

	@GetMapping("/simulateur")
	public String afficherSimulateur(Model model) {
		
		List<InscriptionSessionSimpleModel> inscSM = simulateurService.getInscriptionsSessionSimples();
		model.addAttribute("inscriptionsSession", inscSM);
	
		return "inscriptions_sessions_simulateur";

	}

	@GetMapping("/simulateur/{idInscriptionSession}")
	public String afficherSimulateur2(@PathVariable("idInscriptionSession") String strIdInscriptionSession,
			Model model) {

		Long idInscriptionSession = Long.parseLong(strIdInscriptionSession);
		InscriptionSessionModel inscSM = simulateurService.getInscriptionComplete(idInscriptionSession);
		model.addAttribute("inscriptionSession", inscSM);
		
		return "simulateur";

	}
	
	@ResponseBody
	@GetMapping("/simulateur/{idInscriptionSession}/getCalculateurs")
	public Set<CalculateurModel> getCalculateurs(@PathVariable("idInscriptionSession") String strIdInscriptionSession) {
		
		Long idInscriptionSession = Long.parseLong(strIdInscriptionSession);
		Set<CalculateurModel> calculateurs = simulateurService.getCalculateurs(idInscriptionSession);
		return calculateurs;
		
		
	}
	

	@ResponseBody
	@PostMapping("/simulateur/{idInscriptionSession}/setNoteObjectif")
	public Set<CalculateurModel> setNoteObjectif(@RequestParam("idCalculateur") Long idCalculateur,
			@RequestParam("noteObjectif") Double noteObjectif) {
		
		Set<CalculateurModel> calculateurs = simulateurService.setNoteObjectif(idCalculateur, noteObjectif);
		return calculateurs;

	}
	
	
	@ResponseBody
	@PostMapping("/simulateur/{idInscriptionSession}/annulerNoteObjectif")
	public Set<CalculateurModel> annulerNoteObjectif(@RequestParam("idCalculateur") Long idCalculateur) {
		
		Set<CalculateurModel> calculateurs = simulateurService.annulerNoteObjectif(idCalculateur);
		return calculateurs;

	}

}
