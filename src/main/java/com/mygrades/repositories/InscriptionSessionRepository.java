package com.mygrades.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.InscriptionModule;
import com.mygrades.domain.InscriptionSession;
import com.mygrades.web.simulateur.InscriptionSessionSimpleModel;

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
	
	@Query("SELECT inscS "
			+ "FROM InscriptionSession inscS "
			+ "JOIN FETCH inscS.session ses "
			+ "JOIN FETCH ses.semestre sem "
			+ "JOIN FETCH sem.filiere fil "
			+ "JOIN FETCH inscS.calculateur calcS "
			+ "LEFT JOIN FETCH inscS.inscriptionsModule inscM "
			+ "LEFT JOIN FETCH inscM.module modu "
			+ "LEFT JOIN FETCH inscM.calculateur calcM "
			+ "LEFT JOIN FETCH inscM.inscriptionsDevoir inscD "
			+ "LEFT JOIN FETCH inscD.devoir dev "
			+ "LEFT JOIN FETCH inscD.calculateur calcD "
			+ "JOIN FETCH inscS.etudiant etu "
			+ "JOIN FETCH etu.utilisateur uti "
			+ "WHERE inscS.id = :idInscriptionSession "
			+ "ORDER BY modu.code, dev.nom")
	public InscriptionSession getInscriptionSessionComplete(@Param("idInscriptionSession") Long idInscriptionSession);
	
	@Query("SELECT NEW com.mygrades.web.simulateur.InscriptionSessionSimpleModel(inscS.id, etu.numero, uti.prenom, "
			+ "  uti.nom, sem.nom, ses.nom, fil.nom) "
			+ "FROM InscriptionSession inscS "
			+ "JOIN inscS.semestre sem "
			+ "JOIN sem.filiere fil "
			+ "JOIN inscS.session ses "
			+ "JOIN inscS.etudiant etu "
			+ "JOIN etu.utilisateur uti "
			+ "WHERE inscS.termine = false "
			+ "ORDER BY etu.numero ")
	public List<InscriptionSessionSimpleModel> getInscriptionsSessionSimples();
	
}
