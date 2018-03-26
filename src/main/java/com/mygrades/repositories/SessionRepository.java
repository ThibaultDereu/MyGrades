package com.mygrades.repositories;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.InscriptionDevoir;
import com.mygrades.domain.Module;
import com.mygrades.domain.Session;
import com.mygrades.domain.Utilisateur;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
	
	@Query("SELECT ses "
			+ "FROM Session ses "
			+ "JOIN FETCH ses.semestre sem "
			+ "JOIN FETCH sem.filiere fil "
			+ "WHERE fil.id = :idFiliere "
			+ "AND ses.actif = :actif "
			+ "ORDER BY ses.dateCloture, sem.nom ")
	public List<Session> getSessions(@Param("idFiliere") Long idFiliere, @Param("actif") Boolean actif);
		
	// récupérer tous les modules pouvant être notés (en cours), et les éventuels devoirs rattachés
	@Query("SELECT mod.id AS idModule, mod.code AS codeModule, mod.nom AS nomModule, "
			+ "  dev.id AS idDevoir, dev.nom AS nomDevoir, dev.coefficient AS coefficient, "
			+ "  COUNT(inscD) As nbInscriptions, "
			+ "  SUM(CASE inscD.termine WHEN true THEN 1 ELSE 0 END) AS nbInscriptionsNotees "
			+ "FROM Session ses "
			+ "JOIN ses.inscriptionsSession inscS "
			+ "JOIN inscS.inscriptionsModule inscM "
			+ "JOIN inscM.module mod "
			+ "LEFT JOIN inscM.inscriptionsDevoir inscD "
			+ "LEFT JOIN inscD.devoir dev "
			+ "WHERE ses.id = :idSession "
			+ "GROUP BY mod, dev "
			+ "ORDER BY mod.code, dev.nom ")
	public List<Tuple> getDevoirsAvecModule(@Param("idSession") Long idSession);
		
	
	@Query("SELECT inscS.id AS id, etu.numero AS numeroEtudiant, uti.prenom AS prenomEtudiant, uti.nom AS nomEtudiant, "
			+ "COUNT(inscM) AS nbModules, inscS.termine AS termine, inscS.acquis AS acquis, inscS.note AS note "
			+ "FROM Session ses "
			+ "JOIN ses.inscriptionsSession inscS "
			+ "JOIN inscS.etudiant etu "
			+ "JOIN etu.utilisateur uti "
			+ "LEFT JOIN inscS.inscriptionsModule inscM "
			+ "WHERE ses.id = :idSession "
			+ "GROUP BY inscS.id, etu.numero, uti.prenom, uti.nom, inscS.termine, inscS.acquis, inscS.note "
			+ "ORDER BY etu.numero ")
	public List<Tuple> getInscriptionsSessions(@Param("idSession") Long idSession);
	

	@Query("SELECT uti "
			+ "FROM Utilisateur uti "
			+ "JOIN FETCH uti.profilEtudiant etu "
			+ "LEFT JOIN FETCH uti.profilEnseignant ens "
			+ "WHERE NOT EXISTS ("
			+ "  SELECT insc "
			+ "  FROM etu.inscriptionsSession insc "
			+ "  WHERE insc.session.id = :idSession) "
			+ "ORDER BY etu.numero ")
	public List<Utilisateur> getEtudiantsDispo(@Param("idSession") Long idSession);
	
	// tous les modules présents dans un semestre, et pas encore présents dans une inscription session
	@Query("SELECT mod "
			+ "FROM Module mod "
			+ "WHERE mod in ( "
			+ "  SELECT semMod "
			+ "  FROM InscriptionSession inscS "
			+ "  JOIN inscS.semestre sem "
			+ "  JOIN sem.modules semMod "
			+ "  WHERE inscS.id = :idInscriptionSession "
			+ ") "
			+ "AND NOT EXISTS ( "
			+ "  SELECT inscM "
			+ "  FROM InscriptionModule inscM "
			+ "  WHERE inscM.inscriptionSession.id = :idInscriptionSession "
			+ "  AND inscM.module = mod "
			+ ") "
			+ "ORDER BY mod.code")
	public List<Module> getModulesDispo(@Param("idInscriptionSession") Long idInscriptionSession);
	
	@Query("SELECT inscD.id AS id, dev.nom AS nomDevoir, inscD.note AS note, dev.coefficient AS coefficient,"
			+ "  etu.numero AS numeroEtudiant, uti.prenom AS prenomEtudiant, uti.nom AS nomEtudiant "
			+ "FROM Session ses "
			+ "JOIN ses.inscriptionsSession inscS "
			+ "JOIN inscS.inscriptionsModule inscM "
			+ "JOIN inscM.inscriptionsDevoir inscD "
			+ "JOIN inscD.devoir dev "
			+ "JOIN inscS.etudiant etu "
			+ "JOIN etu.utilisateur uti "
			+ "WHERE ses.id = :idSession "
			+ "AND dev.id = :idDevoir "
			+ "ORDER BY etu.numero ")
	public List<Tuple> getInscriptionsDevoir(@Param("idSession") Long idSession, @Param("idDevoir") Long idDevoir);
	
}
