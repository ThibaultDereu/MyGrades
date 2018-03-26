package com.mygrades.domain;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DomainTest {

	/**
	 * TODO tests restant à écrire à tester :
	 * 
	 * - inscrire un étudiant à une session déjà terminée => erreur. 
	 * 
	 * - inscrire un étudiant à une session alors qu'il a déjà validé ce semestre => erreur. -
	 * inscrire un étudiant ayant déjà suivi ce semestre et échoué => tous les
	 * semestres validés sont recopiés et terminés d'office, et tous les semestres
	 * non validés sont réinscrits.
	 * 
	 * - désinscrire d'une session un étudiant ayant déjà un devoir noté => erreur.
	 * 
	 * - création d'un devoir sur un module qui n'existe pas dans ce semestre ?
	 */

	/**
	 * inscrire un étudiant à un semestre avec modules optionnels et non optionnels
	 * => l'étudiant est inscrit automatiquement aux modules non optionnels, mais
	 * pas aux modules optionnels.
	 */
	@Test
	public void testInscriptionSessionAvecModulesObligatoires() {
		Filiere f1 = new Filiere("f1");

		Semestre s1 = new Semestre(f1, "s1");

		Module m1 = new Module("m1", "A");
		Module m2 = new Module("m2", "B");
		Module m3 = new Module("m3", "C");
		Module m4 = new Module("m4", "D");
		m1.setOptionnel(false);
		m2.setOptionnel(true);
		m3.setOptionnel(false);
		m4.setOptionnel(true);
		s1.addModule(m1);
		s1.addModule(m2);
		s1.addModule(m3);
		s1.addModule(m4);

		Etudiant e1 = new Etudiant();

		Session sess = new Session(s1, "sess");

		InscriptionSession inscS = new InscriptionSession(sess, e1);

		Set<Module> modulesInscrits = new HashSet<>();

		// vérifier que l'inscription est bien dans la session
		Assert.assertTrue(sess.getInscriptionsSession().contains(inscS));

		for (InscriptionModule inscM : inscS.getInscriptionsModule()) {
			modulesInscrits.add(inscM.getModule());
		}

		// vérifier que seuls les modules obligatoires ont été inscrits.
		Assert.assertTrue(modulesInscrits.contains(m1));
		Assert.assertFalse(modulesInscrits.contains(m2));
		Assert.assertTrue(modulesInscrits.contains(m3));
		Assert.assertFalse(modulesInscrits.contains(m4));
	}

	/**
	 * inscrire un étudiant déjà inscrit à cette session => erreur.
	 */
	@Test(expected = IllegalStateException.class)
	public void testInscriptionSession2Fois() {
		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "s1");
		Etudiant e1 = new Etudiant();
		Session sess = new Session(s1, "sess");
		InscriptionSession is1 = new InscriptionSession(sess, e1);
		InscriptionSession is2 = new InscriptionSession(sess, e1);
	}

	/**
	 * inscrire à une session un étudiant ayant déjà une autre inscription session
	 * en cours sur le même semestre => erreur.
	 */
	@Test(expected = IllegalStateException.class)
	public void testInscriptionSemestre2Fois() {
		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "s1");
		Session sess1 = new Session(s1, "sess1");
		Session sess2 = new Session(s1, "sess2");
		Etudiant e1 = new Etudiant();
		InscriptionSession is1 = new InscriptionSession(sess1, e1);
		InscriptionSession is2 = new InscriptionSession(sess2, e1);
	}

	/**
	 * désinscrire un étudiant d'un module optionnel => le module est retiré.
	 */
	@Test
	public void testDesinscrireModuleOptionnel() {
		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "s1");
		Module m1 = new Module("m1", "A");
		m1.setOptionnel(true);
		s1.addModule(m1);
		Session sess = new Session(s1, "sess");
		Etudiant e1 = new Etudiant();
		InscriptionSession is1 = new InscriptionSession(sess, e1);
		InscriptionModule im1 = new InscriptionModule(is1, m1);

		is1.retirerInscriptionModule(im1);

		Assert.assertTrue(is1.getInscriptionsModule().isEmpty());
	}

	/**
	 * désinscrire un étudiant d'un module obligatoire => erreur
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDesinscrireModuleObligatoire() {
		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "s1");
		Module m1 = new Module("m1", "A");
		m1.setOptionnel(false);
		s1.addModule(m1);
		Session sess = new Session(s1, "sess");
		Etudiant e1 = new Etudiant();
		InscriptionSession is1 = new InscriptionSession(sess, e1);

		InscriptionModule im1 = is1.getInscriptionsModule().iterator().next();
		is1.retirerInscriptionModule(im1);
	}

	/**
	 * inscrire un étudiant 2 fois au même module dans une même session => erreur.
	 */
	@Test(expected = IllegalStateException.class)
	public void testInscriptionModule2Fois() {
		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "s1");
		Module m1 = new Module("m1", "A");
		m1.setOptionnel(true);
		s1.addModule(m1);
		Session sess = new Session(s1, "sess");
		Etudiant e1 = new Etudiant();
		InscriptionSession is1 = new InscriptionSession(sess, e1);
		InscriptionModule im1 = new InscriptionModule(is1, m1);
		InscriptionModule im2 = new InscriptionModule(is1, m1);
	}

	/**
	 * créer des devoirs sur un module, puis inscrire des étudiants à ce module =>
	 * les étudiants sont inscrits à ces devoirs
	 */
	@Test
	public void testCreerDevoirsPuisInscrireEtudiant() {
		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "s1");
		Module m1 = new Module("m1", "A");
		s1.addModule(m1);
		m1.setOptionnel(false);
		Session sess = new Session(s1, "sess");
		Devoir d1 = new Devoir(sess, m1, 1);
		Devoir d2 = new Devoir(sess, m1, 1);
		Etudiant e1 = new Etudiant();
		Etudiant e2 = new Etudiant();
		InscriptionSession is1 = new InscriptionSession(sess, e1);
		InscriptionSession is2 = new InscriptionSession(sess, e2);

		InscriptionModule im1 = is1.getInscriptionsModule().iterator().next();
		Set<Devoir> devoirs = new HashSet<>();
		for (InscriptionDevoir id : im1.getInscriptionsDevoir()) {
			devoirs.add(id.getDevoir());
		}
		Assert.assertTrue(devoirs.contains(d1));
		Assert.assertTrue(devoirs.contains(d2));

		InscriptionModule im2 = is2.getInscriptionsModule().iterator().next();
		devoirs = new HashSet<>();
		for (InscriptionDevoir id : im1.getInscriptionsDevoir()) {
			devoirs.add(id.getDevoir());
		}
		Assert.assertTrue(devoirs.contains(d1));
		Assert.assertTrue(devoirs.contains(d2));
	}

	/**
	 * inscrire des étudiants à un module, puis créer un devoir sur ce module =>
	 * l'étudiant est inscrit à ce devoir
	 */
	@Test
	public void testInscrireEtudiantPuisCreerDevoir() {
		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "s1");
		Module m1 = new Module("m1", "A");
		s1.addModule(m1);
		m1.setOptionnel(false);
		Session sess = new Session(s1, "sess1");
		Etudiant e1 = new Etudiant();
		InscriptionSession is = new InscriptionSession(sess, e1);
		Devoir d1 = new Devoir(sess, m1, 1);
		Devoir d2 = new Devoir(sess, m1, 1);
		InscriptionModule im1 = is.getInscriptionsModule().iterator().next();
		Set<Devoir> devoirs = new HashSet<>();
		for (InscriptionDevoir id : im1.getInscriptionsDevoir()) {
			devoirs.add(id.getDevoir());
		}
		Assert.assertTrue(devoirs.contains(d1));
		Assert.assertTrue(devoirs.contains(d2));

	}

	/**
	 * désinscrire un étudiant d'un module avec devoir non noté => OK. désinscrire
	 * un étudiant d'un module avec devoir noté => erreur.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInscriptionModuleAvecDevoir() {
		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "s1");
		Module m1 = new Module("m1", "A");
		Module m2 = new Module("m2", "B");
		Session sess = new Session(s1, "s1");
		Etudiant e1 = new Etudiant();
		InscriptionSession is = new InscriptionSession(sess, e1);
		Devoir d1 = new Devoir(sess, m1, 1);
		Devoir d2 = new Devoir(sess, m2, 1);
		InscriptionModule im1 = new InscriptionModule(is, m1);
		InscriptionModule im2 = new InscriptionModule(is, m2);

		// désinscrire d'un module avec devoir non noté
		is.retirerInscriptionModule(im1);
		Assert.assertTrue(im1.getInscriptionsDevoir().isEmpty());

		// désinscrire d'un module avec devoir noté
		InscriptionDevoir id2 = im2.getInscriptionsDevoir().iterator().next();
		id2.setNote(15d);
		is.retirerInscriptionModule(im2);
	}

	/**
	 * retirer un devoir jamais noté => tous les étudiants sont désinscrits de ce
	 * devoir. retirer un devoir pour lequel il existe une inscription devoir notée
	 * => erreur.
	 */
	@Test(expected = IllegalStateException.class)
	public void testRetirerDevoir() {
		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "s1");
		Module m1 = new Module("m1", "A");
		s1.addModule(m1);
		Session sess = new Session(s1, "s1");
		Etudiant e1 = new Etudiant();
		Etudiant e2 = new Etudiant();
		InscriptionSession is1 = new InscriptionSession(sess, e1);
		InscriptionSession is2 = new InscriptionSession(sess, e2);
		InscriptionModule im1 = new InscriptionModule(is1, m1);
		InscriptionModule im2 = new InscriptionModule(is2, m1);

		// retrait d'un devoir non noté
		Devoir d1 = new Devoir(sess, m1, 1);
		Assert.assertFalse(im1.getInscriptionsDevoir().isEmpty());
		Assert.assertFalse(im2.getInscriptionsDevoir().isEmpty());
		sess.retirerDevoir(d1);
		Assert.assertTrue(im1.getInscriptionsDevoir().isEmpty());
		Assert.assertTrue(im2.getInscriptionsDevoir().isEmpty());

		// retrait d'un devoir noté
		Devoir d2 = new Devoir(sess, m1, 1);
		InscriptionDevoir id1 = im1.getInscriptionsDevoir().iterator().next();
		id1.setNote(12d);
		Assert.assertTrue(id1.isTermine());
		sess.retirerDevoir(d2);
	}

	/**
	 * Vérifier les valeurs calculées par les calculateurs, en faisant varier les
	 * notes aux devoirs et les notes objectifs aux devoirs, module et session.
	 */
	@Test
	public void testsCalculateurs() {
		// préparer les filières et modules
		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "s1");
		Module m1 = new Module("m1", "A");
		m1.setCoefficient(1);
		s1.addModule(m1);
		m1.setOptionnel(true);
		Module m2 = new Module("m2", "B");
		m2.setCoefficient(1);
		s1.addModule(m2);
		m2.setOptionnel(true);
		Module m3 = new Module("m3", "C");
		m3.setCoefficient(2);
		s1.addModule(m3);
		m3.setOptionnel(true);

		// créer une session et des devoirs
		Session sess = new Session(s1, "s1");
		Devoir d11 = new Devoir(sess, m1, 3);
		Devoir d12 = new Devoir(sess, m1, 1);
		Devoir d13 = new Devoir(sess, m1, 5);
		Devoir d21 = new Devoir(sess, m2, 3);
		Devoir d22 = new Devoir(sess, m2, 2);
		Devoir d23 = new Devoir(sess, m2, 1);
		Devoir d31 = new Devoir(sess, m3, 1);
		Devoir d32 = new Devoir(sess, m3, 2);

		// inscrire un étudiant à cette session et à des modules de cette session
		Etudiant e1 = new Etudiant();
		InscriptionSession is1 = new InscriptionSession(sess, e1);
		InscriptionModule im1 = new InscriptionModule(is1, m1);
		InscriptionModule im2 = new InscriptionModule(is1, m2);
		InscriptionModule im3 = new InscriptionModule(is1, m3);
		
		InscriptionDevoir id11 = im1.getInscriptionDevoir(d11);
		InscriptionDevoir id12 = im1.getInscriptionDevoir(d12);
		InscriptionDevoir id13 = im1.getInscriptionDevoir(d13);
		InscriptionDevoir id21 = im2.getInscriptionDevoir(d21);
		InscriptionDevoir id22 = im2.getInscriptionDevoir(d22);
		InscriptionDevoir id23 = im2.getInscriptionDevoir(d23);
		InscriptionDevoir id31 = im3.getInscriptionDevoir(d31);
		InscriptionDevoir id32 = im3.getInscriptionDevoir(d32);

		// le delta pour les contrôles d'égalité entre variables de type double
		double d = 0.001d;

		// module dont tous les devoirs sont notés
		Assert.assertEquals(0d, id11.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(20d, id11.getCalculateur().getNoteMax(), d);
		id11.setNote(10.5);
		id12.setNote(15);
		id13.setNote(8);
		Assert.assertEquals(10.5d, id11.getCalculateur().getNoteMin(), d);
		Assert.assertEquals((10.5 * 3 + 15 * 1 + 8 * 5) / (3 + 1 + 5), im1.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(im1.getCalculateur().getNoteMin(), im1.getCalculateur().getNoteMax(), d);

		// une inscription devoir change de note
		id13.setNote(20);
		Assert.assertEquals((10.5 * 3 + 15 * 1 + 20 * 5) / (3 + 1 + 5), im1.getCalculateur().getNoteMin(), d);

		// module dont une partie des devoirs sont notés suite à une annulation de note
		id11.annulerNote();
		Assert.assertEquals((0d * 3 + 15d * 1 + 20d * 5) / (3 + 1 + 5), im1.getCalculateur().getNoteMin(), d);
		Assert.assertEquals((20d * 3 + 15d * 1 + 20d * 5) / (3 + 1 + 5), im1.getCalculateur().getNoteMax(), d);

		// module ayant une note objectif
		Assert.assertEquals(0d, im2.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(20d, im2.getCalculateur().getNoteMax(), d);
		im2.getCalculateur().setNoteObjectif(17.25d);
		Assert.assertEquals(0d, im2.getCalculateur().getNoteMin(), d);
		printNotes(is1, im1, id11, id12, id13, im2, id21, id22, id23, im3, id31,
				 id32);
		
		// pour pouvoir avoir 17.25 au module 2, il faut au moins avoir 14.5 au devoir
		// 21, etc...
		Assert.assertEquals(14.5, id21.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(11.75, id22.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(3.5, id23.getCalculateur().getNoteMin(), d);
		
		// l'objectif du module change aussi les notes acquises de la session.
		Assert.assertEquals(7.5069, is1.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(19.1736, is1.getCalculateur().getNoteMax(), d);
		
		// si on prévoit d'avoir 3 au module 2, cela veut dire qu'on ne peut pas
		// dépasser 6 au devoir 21, etc...
		Assert.assertEquals(20d, id21.getCalculateur().getNoteMax(), d);
		im2.getCalculateur().setNoteObjectif(3d);
		Assert.assertEquals(6, id21.getCalculateur().getNoteMax(), d);
		Assert.assertEquals(9, id22.getCalculateur().getNoteMax(), d);
		Assert.assertEquals(18, id23.getCalculateur().getNoteMax(), d);
				
		// annuler l'objectif
		im2.getCalculateur().setNoteObjectif(null);
		Assert.assertEquals(0, im2.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(20, im2.getCalculateur().getNoteMax(), d);
		Assert.assertEquals(0, id21.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(20, id21.getCalculateur().getNoteMax(), d);

		// module ayant une note objectif et une partie des devoirs notés
		im2.getCalculateur().setNoteObjectif(10d);
		id21.setNote(7);
		Assert.assertEquals(9.5, id22.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(19.5, id22.getCalculateur().getNoteMax(), d);
		Assert.assertEquals(0, id23.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(20, id23.getCalculateur().getNoteMax(), d);

		// module sans note, ayant un module frère avec une partie des devoirs notés, un
		// autre frère avec un objectif, et une session ayant un objectif
		is1.getCalculateur().setNoteObjectif(12d);
		Assert.assertEquals(9.2777, im3.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(12.6111, im3.getCalculateur().getNoteMax(), d);

		// du coup, les devoirs du module 3 doivent permettre au module 3 d'atteindre
		// ses notes minimale et maximale requises
		Assert.assertEquals(0, id31.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(20, id31.getCalculateur().getNoteMax(), d);
		Assert.assertEquals(3.9166, id32.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(18.9166, id32.getCalculateur().getNoteMax(), d);

		// devoir avec coeff à zéro
		id11.setNote(20);
		d11.setCoefficient(0);
		Assert.assertEquals(19.1666, im1.getCalculateur().getNoteMin(), d);
		Assert.assertEquals(19.1666, im1.getCalculateur().getNoteMax(), d);

		printNotes(is1, im1, id11, id12, id13, im2, id21, id22, id23, im3, id31,
		 id32);
	}

	/**
	 * Vérifier que le calcul des modules et sessions acquis est correct. Si le
	 * statut de ne peut pas être déterminé, acquisReel doit être null. Si la note
	 * de la session est sous la moyenne, échec, Sinon, si la note de la session est
	 * au-dessus de la moyenne, mais qu'un module est sous la note de compensation,
	 * échec.
	 */
	@Test
	public void testCalculAcquisReel() {
		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "s1");
		Module m1 = new Module("m1", "A");
		s1.addModule(m1);
		m1.setOptionnel(true);
		Module m2 = new Module("m2", "B");
		s1.addModule(m2);
		m2.setOptionnel(true);
		Session sess = new Session(s1, "s1");
		Etudiant e1 = new Etudiant();
		InscriptionSession is1 = new InscriptionSession(sess, e1);
		InscriptionModule im1 = new InscriptionModule(is1, m1);
		InscriptionModule im2 = new InscriptionModule(is1, m2);
		Devoir d11 = new Devoir(sess, m1, 1);
		Devoir d12 = new Devoir(sess, m1, 1);
		Devoir d21 = new Devoir(sess, m2, 1);
		Devoir d22 = new Devoir(sess, m2, 1);
		InscriptionDevoir id11 = im1.getInscriptionDevoir(d11);
		InscriptionDevoir id12 = im1.getInscriptionDevoir(d12);
		InscriptionDevoir id21 = im2.getInscriptionDevoir(d21);
		InscriptionDevoir id22 = im2.getInscriptionDevoir(d22);

		// si un devoir n'est pas encore noté, le statut du module est indéterminé
		Assert.assertEquals(im1.getCalculateur().getAcquisReel(), null);
		id11.setNote(10);
		Assert.assertEquals(im1.getCalculateur().getAcquisReel(), null);

		// si la moyenne des devoirs est au-dessus de 10, le module est acquis
		id12.setNote(15);
		Assert.assertEquals(im1.getCalculateur().getAcquisReel(), true);

		// si la moyenne du module est sous le seuil de compensation, le module n'est
		// pas acquis.
		m1.setNoteSeuil(8);
		id12.setNote(2);
		Assert.assertEquals(im1.getCalculateur().getAcquisReel(), false);

		// par contre, si la moyenne du module est au-dessus du seuil mais que la note
		// globale du semestre n'est pas encore déterminée, le statut du module est
		// encore incalculable. (on ne sait pas encore si on peut compenser)
		m1.setNoteSeuil(2);
		id12.setNote(3);
		Assert.assertEquals(im1.getCalculateur().getAcquisReel(), null);

		// la note du semestre est sous la moyenne => le semestre est non acquis, ainsi
		// que tous les modules sous la moyenne.
		id21.setNote(8);
		id22.setNote(13);
		Assert.assertEquals(is1.getCalculateur().getAcquisReel(), false);
		Assert.assertEquals(im1.getCalculateur().getAcquisReel(), false);
		Assert.assertEquals(im2.getCalculateur().getAcquisReel(), true);

		// Si la note du semestre dépasse la moyenne, même les modules sous la moyenne
		// sont acquis.
		id21.setNote(20);
		Assert.assertEquals(is1.getCalculateur().getAcquisReel(), true);
		Assert.assertEquals(im1.getCalculateur().getAcquisReel(), true);
		Assert.assertEquals(im2.getCalculateur().getAcquisReel(), true);

		// Même si la note du semestre dépasse la moyenne, si au moins un module est
		// sous le seuil de compensation, le semestre est non acquis.
		m1.setNoteSeuil(9);
		id11.setNote(8);
		Assert.assertEquals(is1.getCalculateur().getAcquisReel(), false);
		Assert.assertEquals(im1.getCalculateur().getAcquisReel(), false);
		Assert.assertEquals(im2.getCalculateur().getAcquisReel(), true);

		//printNotes(is1, im1, id11, id12, im2, id21, id22);
	}

	// pour debugger le calculateur
	private void printNotes(AbstractInscription... inscs) {
		for (AbstractInscription insc : inscs) {
			System.out.println(String.format(
					"Inscription '%s' : note min = %s, note max = %s, note objectif = %s, note réelle = %s, acquis réel = %s\r\n",
					insc, insc.getCalculateur().getNoteMin(), insc.getCalculateur().getNoteMax(),
					insc.getCalculateur().getNoteObjectif(), insc.getCalculateur().getNoteReelle(),
					insc.getCalculateur().getAcquisReel()));
		}
	}
	
	@Test
	public void testCloturerSession() {
		
		/* 
		 * TODO vérifier qu'après clôture, toutes les inscriptions sont terminés, tous les acquis non nuls, toutes les notes réelles non nulles.
		 * TODO clôture d'une session => génération d'un rattrapage si la session était numéro 1 et 
		 * TODO vérifier qu'il n'est pas possible de clôturer une session dont un module ne contient aucun devoir.
		 * TODO une session avec un devoir non noté => exception
		 * TODO essayer de clôturer une session clôturée
		 */
		Filiere f1 = new Filiere("f1");
		Semestre s1 = new Semestre(f1, "f1");
		Module m1 = new Module("m1", "A");
		Module m2 = new Module("m2", "B");
		Module m3 = new Module("m3", "C");
		m1.setOptionnel(false);
		m2.setOptionnel(false);
		m3.setOptionnel(false);
		s1.addModule(m1);
		s1.addModule(m2);
		s1.addModule(m3);
		
		Session sess = new Session(s1, "s1");
		Etudiant e1 = new Etudiant();
		Etudiant e2 = new Etudiant();
		Etudiant e3 = new Etudiant();
		InscriptionSession is1 = new InscriptionSession(sess, e1);
		InscriptionSession is2 = new InscriptionSession(sess, e2);
		InscriptionSession is3 = new InscriptionSession(sess, e3);
		Devoir d11 = new Devoir(sess, m1, 1);
		Devoir d12 = new Devoir(sess, m1, 1);
		Devoir d21 = new Devoir(sess, m2, 1);
		Devoir d22 = new Devoir(sess, m2, 1);
		Devoir d31 = new Devoir(sess, m3, 1);
		Devoir d32 = new Devoir(sess, m3, 1);
		
		for (InscriptionSession inscS : sess.getInscriptionsSession()) {
			for (InscriptionModule inscM : inscS.getInscriptionsModule()) {
				for (InscriptionDevoir inscD : inscM.getInscriptionsDevoir()) {
					inscD.setNote(0);
				}
			}
		}
		
		sess.cloturer();
		
	}


}
