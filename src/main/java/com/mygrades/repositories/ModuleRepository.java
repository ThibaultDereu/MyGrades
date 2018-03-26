package com.mygrades.repositories;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.Module;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Long> {
	List<Module> findAllByOrderByCode();
	
	@Query(value = "select mo.id as idModule, mo.code as codeModule, mo.nom as nomModule, mo.nbCredits as nbCredits, mo.optionnel as optionnel, "
			+ "mo.coefficient as coefficient, mo.rattrapable as rattrapable, mo.noteSeuil as noteSeuil, en.id as idEnseignant,"
			+ "ut.nom as nomEnseignant, ut.prenom as prenomEnseignant "
			+ "from Module mo left join mo.enseignant en left join en.utilisateur ut order by mo.code")
	List<Tuple> getAllModulesAvecEnseignant();
}
