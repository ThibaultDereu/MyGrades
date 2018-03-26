package com.mygrades.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mygrades.domain.Enseignant;
import com.mygrades.domain.Etudiant;
import com.mygrades.domain.Module;
import com.mygrades.domain.Utilisateur;
import com.mygrades.repositories.ModuleRepository;
import com.mygrades.repositories.UtilisateurRepository;
import com.mygrades.web.administration.EnseignantModel;
import com.mygrades.web.administration.EtudiantModel;
import com.mygrades.web.administration.UtilisateurModel;

@Service
public class UtilisateursService {

	@Autowired
	UtilisateurRepository repUtilisateur;
	
	@Autowired
	ModuleRepository repModule;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public List<UtilisateurModel> getListeUtilisateurs() {

		List<UtilisateurModel> utilisateursModel = new ArrayList<>();
		List<Tuple> utilisateurs = repUtilisateur.getListeUtilisateurs();

		for (Tuple u : utilisateurs) {
			UtilisateurModel um = new UtilisateurModel();
			um.setId(u.get("id", Long.class));
			um.setAdmin(u.get("admin", Boolean.class));
			um.setEmail(u.get("email", String.class));
			um.setNom(u.get("nom", String.class));
			um.setPrenom(u.get("prenom", String.class));
			um.setIdEnseignant(u.get("idEnseignant", Long.class));
			um.setIdEtudiant(u.get("idEtudiant", Long.class));
			utilisateursModel.add(um);
		}

		return utilisateursModel;
	}

	@Transactional
	public void creerUtilisateur(UtilisateurModel um) {

		Utilisateur u = new Utilisateur(um.getEmail(), um.getPrenom(), um.getNom());
		u.setAdmin(um.getAdmin());
		if (!um.getPassword().isEmpty()) {
			u.setPassword(passwordEncoder.encode(um.getPassword()));
		}

		repUtilisateur.save(u);
	}

	public UtilisateurModel getUtilisateur(Long idUtilisateur) {

		Utilisateur u = repUtilisateur.findOne(idUtilisateur);
		UtilisateurModel um = new UtilisateurModel();
		um.setAdmin(u.isAdmin());
		um.setEmail(u.getEmail());
		um.setId(u.getId());
		um.setIdEnseignant(u.getProfilEnseignant() != null ? u.getProfilEnseignant().getId() : null);
		um.setIdEtudiant(u.getProfilEtudiant() != null ? u.getProfilEtudiant().getId() : null);
		um.setNom(u.getNom());
		um.setPrenom(u.getPrenom());

		return um;
	}

	@Transactional
	public void modifierUtilisateur(UtilisateurModel um) {

		Utilisateur u = repUtilisateur.findOne(um.getId());
		u.setAdmin(um.getAdmin());
		u.setEmail(um.getEmail());
		u.setNom(um.getNom());
		u.setPrenom(um.getPrenom());
		// modifier le mot de passe seulement s'il n'est pas laissé blanc
		if (!um.getPassword().isEmpty()) {
			u.setPassword(passwordEncoder.encode(um.getPassword()));
		}

	}

	@Transactional
	public void supprimerUtilisateur(Long id) {
		repUtilisateur.delete(id);
	}

	public EtudiantModel getProfilEtudiant(Long idUtilisateur) {
		Utilisateur u = repUtilisateur.findOne(idUtilisateur);

		EtudiantModel em = new EtudiantModel();
		em.setIdUtilisateur(u.getId());
		em.setNom(u.getNom());
		em.setPrenom(u.getPrenom());

		Etudiant etudiant = u.getProfilEtudiant();

		if (etudiant != null) {
			em.setNumero(etudiant.getNumero());
		}

		return em;
	}

	@Transactional
	public void creerProfilEtudiant(EtudiantModel etudiantModel) {
		Utilisateur utilisateur = repUtilisateur.findOne(etudiantModel.getIdUtilisateur());
		Etudiant etudiant = new Etudiant(etudiantModel.getNumero(), utilisateur);
		utilisateur.setProfilEtudiant(etudiant);
		repUtilisateur.save(utilisateur);
	}
	
	@Transactional
	public void modifierEtudiant(EtudiantModel etudiantModel) {
		Utilisateur utilisateur = repUtilisateur.findOne(etudiantModel.getIdUtilisateur());
		Etudiant etudiant = utilisateur.getProfilEtudiant();
		etudiant.setNumero(etudiantModel.getNumero());
		repUtilisateur.save(utilisateur);		
	}
	
	@Transactional
	public void supprimerEtudiant(Long idUtilisateur) {
		Utilisateur utilisateur = repUtilisateur.findOne(idUtilisateur);
		utilisateur.setProfilEtudiant(null);
		repUtilisateur.save(utilisateur);
	}
	
	public EnseignantModel getProfilEnseignant(Long idUtilisateur) {
		Utilisateur u = repUtilisateur.findOne(idUtilisateur);

		EnseignantModel enseignantModel = new EnseignantModel();
		enseignantModel.setIdUtilisateur(u.getId());
		enseignantModel.setNom(u.getNom());
		enseignantModel.setPrenom(u.getPrenom());

		Enseignant enseignant = u.getProfilEnseignant();

		if (enseignant != null) {
			List<Module> modules = enseignant.getModules();
			List<Long> modulesModel = new ArrayList<>();
			
			for (Module m : modules) {
				modulesModel.add(m.getId());
			}
			
			enseignantModel.setModules(modulesModel);
		}

		return enseignantModel;
	}

	@Transactional
	public void creerProfilEnseignant(EnseignantModel enseignantModel) {
		Utilisateur utilisateur = repUtilisateur.findOne(enseignantModel.getIdUtilisateur());
		Enseignant enseignant = new Enseignant(utilisateur);
		
		// récupérer tous les modules enseignés en provenance du formulaire
		List<Module> modules = new ArrayList<>();
		for (Long idModule : enseignantModel.getModules()) {
			Module m = repModule.findOne(idModule);
			modules.add(m);
		}
		enseignant.setModules(modules);
		
		utilisateur.setProfilEnseignant(enseignant);
		repUtilisateur.save(utilisateur);
		
	}
	
	@Transactional
	public void supprimerEnseignant(Long idUtilisateur) {
		Utilisateur utilisateur = repUtilisateur.findOne(idUtilisateur);
		utilisateur.setProfilEnseignant(null);
		repUtilisateur.save(utilisateur);
	}
	
	
	public List<EnseignantModel> getListeEnseignants() {
		List<EnseignantModel> enseignantsModel = new ArrayList<>();
		List<Tuple> utilisateurs = repUtilisateur.getListeEnseignants();
		
		for (Tuple u : utilisateurs) {
			EnseignantModel em = new EnseignantModel();
			em.setId(u.get("idEnseignant", Long.class));
			em.setIdUtilisateur(u.get("idUtilisateur", Long.class));
			em.setNom(u.get("nom", String.class));
			em.setPrenom(u.get("prenom", String.class));
			enseignantsModel.add(em);
		}
		
		return enseignantsModel;
		
	}
	

}
