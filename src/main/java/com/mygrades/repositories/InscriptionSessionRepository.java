package com.mygrades.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.InscriptionModule;
import com.mygrades.domain.InscriptionSession;

@Repository
public interface InscriptionSessionRepository extends CrudRepository<InscriptionSession, Long> {
	
	@Query("SELECT DISTINCT inscM "
			+ "FROM InscriptionModule inscM "
			+ "JOIN FETCH inscM.module mod "
			+ "LEFT JOIN FETCH inscM.inscriptionsDevoir inscD "
			+ "LEFT JOIN FETCH inscD.devoir dev "
			+ "WHERE inscM.inscriptionSession.id = :idInscriptionSession "
			+ "ORDER BY mod.code ")
	public List<InscriptionModule> getInscriptionsModule(@Param("idInscriptionSession") Long idInscriptionSession);

	@Query("SELECT inscS "
			+ "FROM InscriptionSession inscS "
			+ "JOIN FETCH inscS.etudiant etu "
			+ "JOIN FETCH etu.utilisateur "
			+ "WHERE inscS.id = :idInscriptionSession")
	public InscriptionSession getInscriptionSession(@Param("idInscriptionSession") Long idInscriptionSession);
	
}
