package com.mygrades.repositories;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.Filiere;
import com.mygrades.web.sessions.FiliereSessionsModel;

@Repository
public interface FiliereRepository extends CrudRepository<Filiere, Long> {
	public Filiere findByNom(String nom);
		
	// Pour limiter un select sur chaque filiere (ce que ferait findAllByOrderByNom)
	@Query(value = "SELECT fi.id AS id, fi.nom AS nom, COUNT(se) AS nbSemestres "
			+ "FROM Filiere fi LEFT JOIN fi.semestres se GROUP BY fi.id, fi.nom ORDER BY fi.nom")
	public List<Tuple> getSortedListFilieres();
	

	@Query(value = "SELECT new com.mygrades.web.sessions.FiliereSessionsModel(fi.id, fi.nom, count(ses)) "
			+ "FROM Session ses "
			+ "RIGHT JOIN ses.semestre sem "
			+ "  ON ses.actif = true "
			+ "RIGHT JOIN sem.filiere fi "
			+ "GROUP BY fi "
			+ "ORDER BY fi.nom ")
	public List<FiliereSessionsModel> getFilieresSessions();
}
