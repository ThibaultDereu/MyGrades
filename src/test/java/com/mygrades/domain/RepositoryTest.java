package com.mygrades.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.mygrades.repositories.EtudiantRepository;
import com.mygrades.repositories.FiliereRepository;
import com.mygrades.repositories.ModuleRepository;
import com.mygrades.repositories.SessionRepository;
import com.mygrades.web.administration.FiliereModel;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTest {
	@Autowired
	FiliereRepository repFiliere;

	@Autowired
	ModuleRepository repModule;

	@Autowired
	SessionRepository repSession;

	@Autowired
	EtudiantRepository repEtudiant;

	@PersistenceContext
	private EntityManager em;

	// @Test
	// @Transactional
	// @Rollback(false)
	// public void testToto() {
	// /*
	// * tester la création chaînée par cascadeType.persiste,
	// * puis la suppression chaîne par voie d'orphan removal.
	// */
	//
	// Module m1 = new Module("m1");
	// m1.setOptionnel(false);
	// repModule.save(m1);
	//
	// Filiere f1 = new Filiere("f1");
	// Semestre s1 = new Semestre(f1, "s1");
	// s1.addModule(m1);
	// repFiliere.save(f1);
	//
	// Etudiant e1 = new Etudiant();
	// repEtudiant.save(e1);
	//
	// Session sess = new Session(s1);
	// Devoir d1 = new Devoir(sess, m1, 1);
	// InscriptionSession is1 = new InscriptionSession(sess, e1);
	// repSession.save(sess);
	//
	// sess.retirerInscriptionSession(is1);
	// repSession.save(sess);
	//
	// }

	@Test
	@Rollback(false)
	@Transactional
	public void testToto2() {
		/*
		 * tester la création chaînée par cascadeType.persiste, puis la suppression
		 * chaîne par voie d'orphan removal.
		 */

		Module m1 = new Module("m1", "A");
		m1.setOptionnel(false);
		repModule.save(m1);

		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "s1");
		s1.addModule(m1);

		repFiliere.save(f1);
		
		Session sess = new Session(s1, "sess");
		repSession.save(sess);
		
		
		
		
		f1.retirerSemestre(s1);
		repFiliere.save(f1);
		
		repFiliere.delete(f1.getId());
	
		
	}

}
