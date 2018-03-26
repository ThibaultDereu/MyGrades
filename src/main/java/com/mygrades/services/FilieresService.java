package com.mygrades.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mygrades.domain.Enseignant;
import com.mygrades.domain.Filiere;
import com.mygrades.domain.Module;
import com.mygrades.domain.Semestre;
import com.mygrades.domain.Utilisateur;
import com.mygrades.repositories.FiliereRepository;
import com.mygrades.repositories.ModuleRepository;
import com.mygrades.repositories.SemestreRepository;
import com.mygrades.repositories.UtilisateurRepository;
import com.mygrades.web.administration.FiliereModel;
import com.mygrades.web.administration.ModuleModel;
import com.mygrades.web.administration.ModuleModelSimple;
import com.mygrades.web.administration.SemestreModel;

@Service
public class FilieresService {

	@Autowired
	FiliereRepository repFiliere;

	@Autowired
	SemestreRepository repSemestre;

	@Autowired
	ModuleRepository repModule;
	
	@Autowired
	UtilisateurRepository repUtilisateur;

	public List<FiliereModel> getListFilieres() {
		List<FiliereModel> listFilieres = new ArrayList<>();

		for (Tuple fi : repFiliere.getSortedListFilieres()) {
			FiliereModel fiMo = new FiliereModel(fi.get("id", Long.class), fi.get("nom", String.class));
			fiMo.setNbSemestres(fi.get("nbSemestres", Long.class));
			listFilieres.add(fiMo);
		}
		return listFilieres;
	}

	@Transactional
	public void creerFiliere(FiliereModel filiereModel) {
		Filiere filiere = new Filiere(filiereModel.getNom());
		repFiliere.save(filiere);
	}

	public FiliereModel getFiliere(Long id) {
		Filiere filiere = repFiliere.findOne(id);
		FiliereModel filiereModel = new FiliereModel(filiere.getId(), filiere.getNom());
		return filiereModel;
	}

	@Transactional
	public void modifierFiliere(FiliereModel filiereModel) {
		Filiere filiere = repFiliere.findOne(filiereModel.getId());
		filiere.setNom(filiereModel.getNom());
		repFiliere.save(filiere);
	}

	@Transactional
	public void supprimerFiliere(FiliereModel filiereModel) {
		Filiere filiere = repFiliere.findOne(filiereModel.getId());
		repFiliere.delete(filiere);
	}

	@Transactional
	public void creerSemestre(Long idFiliere, SemestreModel semestreModel) {
		Filiere filiere = repFiliere.findOne(idFiliere);
		new Semestre(filiere, semestreModel.getNom());
		repFiliere.save(filiere);
	}

	@Transactional
	public List<SemestreModel> getSemestres(Long idFiliere) {
		Filiere fi = repFiliere.findOne(idFiliere);
		
		// utilisation d'un ArrayList pour pouvoir trier
		List<Semestre> semestres = new ArrayList<>(fi.getSemestres());
		
		// tri en utilisant un comparateur
		Comparator<Semestre> semestreComparator = Comparator.comparing(Semestre::getNom);
		semestres.sort(semestreComparator);
		List<SemestreModel> listSemestresModel = new ArrayList<>();

		for (Semestre se : semestres) {
			SemestreModel seMo = new SemestreModel(se.getId(), se.getNom());
			seMo.setNbModules(se.getModules().size());
			listSemestresModel.add(seMo);
		}
		return listSemestresModel;
	}

	public SemestreModel getSemestre(Long idSemestre) {
		Semestre semestre = repSemestre.findOne(idSemestre);
		SemestreModel semestreModel = new SemestreModel(semestre.getId(), semestre.getNom());
		return semestreModel;
	}

	@Transactional
	public void modifierSemestre(SemestreModel semestreModel) {
		Semestre semestre = repSemestre.findOne(semestreModel.getId());
		semestre.setNom(semestreModel.getNom());
		repSemestre.save(semestre);
	}

	@Transactional
	public void supprimerSemestre(SemestreModel semestreModel) {
		// L'entité filière est maîtresse de l'entité semestre pour la suppression.
		// Le semestre est supprimé grâce à orphanRemoval.
		Semestre semestre = repSemestre.findOne(semestreModel.getId());
		Filiere filiere = semestre.getFiliere();
		filiere.retirerSemestre(semestre);
		repFiliere.save(filiere);
	}

	// récupérer une liste de modules liés à un semestre
	public List<ModuleModel> getModules(Long idSemestre) {
		List<ModuleModel> modulesModel = new ArrayList<>();
		Semestre semestre = repSemestre.findOne(idSemestre);
		List<Module> modules = semestre.getModules();
		List<Module> sortedModules = new ArrayList<>(modules);
		sortedModules.sort((Module m1, Module m2) -> m1.getCode().compareTo(m2.getCode()));

		for (Module mod : sortedModules) {
			ModuleModel modMod = new ModuleModel(mod.getId(), mod.getCode());
			modMod.setCode(mod.getCode());
			modMod.setCoefficient(mod.getCoefficient());
			modMod.setNbCredits(mod.getNbCredits());
			modMod.setNom(mod.getNom());
			modMod.setNomEnseignant("TODO");
			modMod.setNoteSeuil(mod.getNoteSeuil());
			modMod.setOptionnel(mod.isOptionnel());
			modulesModel.add(modMod);
		}
		return modulesModel;
	}

	// récupérer la liste des modules disponibles pour un semestre.
	// idSemestre peut être nul => tous les modules sont retournés.
	public List<ModuleModelSimple> getModulesDispo(Long idSemestre) {
		List<ModuleModelSimple> modulesModel = new ArrayList<>();
		Semestre semestre = repSemestre.findOne(idSemestre);
		List<Module> modules = repModule.findAllByOrderByCode();
		List<Module> modulesSemestre = semestre.getModules();

		for (Module mod : modules) {		
			if (!modulesSemestre.contains(mod)) {
				ModuleModelSimple modMod = new ModuleModelSimple(mod.getId(), mod.getCode(), mod.getNom());
				modMod.setCode(mod.getCode());
				modMod.setNom(mod.getNom());
				modulesModel.add(modMod);
			}
		}
		return modulesModel;
	}
	
	// ajouter un module dans un semestre
	@Transactional
	public void ajouterModuleSemestre(Long idSemestre, Long idModule) {
		Semestre semestre = repSemestre.findOne(idSemestre);
		Module module = repModule.findOne(idModule);
		semestre.addModule(module);
		repSemestre.save(semestre);
	}

	// retirer un module d'un semestre
	@Transactional
	public void retirerModuleSemestre(Long idSemestre, Long idModule) {
		Semestre semestre = repSemestre.findOne(idSemestre);
		Module module = repModule.findOne(idModule);
		semestre.removeModule(module);
		repSemestre.save(semestre);
	}

//	// Liste de tous les modules existants
//	@Transactional
//	public List<ModuleModel> getAllModules() {
//		List<ModuleModel> modulesModel = new ArrayList<>();
////		List<Module> modules = repModule.findAllByOrderByCode();
//		List<Module> modules = repModule.getAllModulesAvecEnseignant();
//
//		for (Module mod : modules) {
//						
//			ModuleModel mm = new ModuleModel(mod.getId(), mod.getCode());
//			mm.setNom(mod.getNom());
//			mm.setCoefficient(mod.getCoefficient());
//			mm.setNbCredits(mod.getNbCredits());
//			mm.setNoteSeuil(mod.getNoteSeuil());
//			mm.setOptionnel(mod.isOptionnel());
//			mm.setRattrapable(mod.isRattrapable());
//			
//			Enseignant ens = mod.getEnseignant();
//			if (ens != null) {
//				Utilisateur uti = ens.getUtilisateur();
////				mm.setIdEnseignant(ens.getId());
////				mm.setNomEnseignant(uti.getNom());
////				mm.setPrenomEnseignant(uti.getPrenom());
//			}
//	
//			modulesModel.add(mm);
//		}
//
//		return modulesModel;
//	}
	
	
	// Liste de tous les modules existants
	@Transactional
	public List<ModuleModel> getAllModules() {
		List<ModuleModel> modulesModel = new ArrayList<>();
		List<Tuple> modules = repModule.getAllModulesAvecEnseignant();

		for (Tuple module : modules) {
						
			ModuleModel mm = new ModuleModel(module.get("idModule", Long.class), module.get("codeModule", String.class));
			mm.setNom(module.get("nomModule", String.class));
			mm.setCoefficient(module.get("coefficient", Integer.class));
			mm.setNbCredits(module.get("nbCredits", Integer.class));
			mm.setNoteSeuil(module.get("noteSeuil", Double.class));
			mm.setOptionnel(module.get("optionnel", Boolean.class));
			mm.setRattrapable(module.get("rattrapable", Boolean.class));
			mm.setIdEnseignant(module.get("idEnseignant", Long.class));
			mm.setNomEnseignant(module.get("nomEnseignant", String.class));
			mm.setPrenomEnseignant(module.get("prenomEnseignant", String.class));
				
			modulesModel.add(mm);
		}

		return modulesModel;
	}
	
	// récupérer une liste de tous les modules en version light.
	public List<ModuleModelSimple> getAllModulesSimple() {
		List<ModuleModelSimple> modulesModel = new ArrayList<>();
		List<Module> modules = repModule.findAllByOrderByCode();

		for (Module m : modules) {
			ModuleModelSimple mm = new ModuleModelSimple(m.getId(), m.getCode(), m.getNom());
			modulesModel.add(mm);
		}

		return modulesModel;
	}

	public ModuleModel getModule(Long idModule) {
		Module m = repModule.findOne(idModule);
		ModuleModel mm = new ModuleModel(m.getId(), m.getCode());
		mm.setNom(m.getNom());
		mm.setCoefficient(m.getCoefficient());
		mm.setNbCredits(m.getNbCredits());
		mm.setNoteSeuil(m.getNoteSeuil());
		mm.setOptionnel(m.isOptionnel());
		mm.setRattrapable(m.isRattrapable());
		if (m.getEnseignant() != null) {
			mm.setIdEnseignant(m.getEnseignant().getId());
			mm.setPrenomEnseignant(m.getEnseignant().getUtilisateur().getPrenom());
			mm.setNomEnseignant(m.getEnseignant().getUtilisateur().getNom());
		}

		return mm;
	}

	@Transactional
	public void creerModule(ModuleModel moduleModel) {
		Module module = new Module(moduleModel.getNom(), moduleModel.getCode());
		module.setCoefficient(moduleModel.getCoefficient());
		module.setNbCredits(moduleModel.getNbCredits());
		module.setNoteSeuil(moduleModel.getNoteSeuil());
		module.setOptionnel(moduleModel.getOptionnel());
		module.setRattrapable(moduleModel.getRattrapable());
		
		if (moduleModel.getIdEnseignant() != null) {
			Utilisateur utilisateur = repUtilisateur.findByProfilEnseignantId(moduleModel.getIdEnseignant());
			Enseignant enseignant = utilisateur.getProfilEnseignant();
			module.setEnseignant(enseignant);
		} else {
			module.setEnseignant(null);
		}

		repModule.save(module);
	}

	@Transactional
	public void modifierModule(ModuleModel moduleModel) {

		Module module = repModule.findOne(moduleModel.getId());
		module.setNom(moduleModel.getNom());
		module.setCode(moduleModel.getCode());
		module.setCoefficient(moduleModel.getCoefficient());
		module.setNbCredits(moduleModel.getNbCredits());
		module.setNoteSeuil(moduleModel.getNoteSeuil());
		module.setOptionnel(moduleModel.getOptionnel());
		module.setRattrapable(moduleModel.getRattrapable());

		if (moduleModel.getIdEnseignant() != null) {
			Utilisateur utilisateur = repUtilisateur.findByProfilEnseignantId(moduleModel.getIdEnseignant());
			Enseignant enseignant = utilisateur.getProfilEnseignant();
			module.setEnseignant(enseignant);
		} else {
			module.setEnseignant(null);
		}

		repModule.save(module);
	}

	@Transactional
	public void supprimerModule(ModuleModel moduleModel) {
		repModule.delete(moduleModel.getId());
	}
	
}
