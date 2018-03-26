package com.mygrades.repositories;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.Utilisateur;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {

	public List<Utilisateur> findAllByOrderByEmail();
	
	@Query(value = "select ut.id as id, ut.admin as admin, ut.email as email, "
			+ "  ut.nom as nom, ut.prenom as prenom, "
			+ "  et.id as idEtudiant, en.id as idEnseignant "
			+ "from Utilisateur ut "
			+ "left join ut.profilEtudiant et "
			+ "left join ut.profilEnseignant en "
			+ "order by ut.email ")
	public List<Tuple> getListeUtilisateurs();
	
	@Query(value = "select ut.id as idUtilisateur, ut.prenom as prenom, ut.nom as nom, en.id as idEnseignant "
			+ " from Utilisateur ut join ut.profilEnseignant en order by ut.nom, ut.prenom")
	public List<Tuple> getListeEnseignants();
	
	public Utilisateur findByProfilEnseignantId(Long idEnseignant);
	
	public Utilisateur findByEmail(String email);
	
}
