package com.mygrades.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractInscription {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
		
	public abstract Double getNote();
	
	public abstract Set<? extends AbstractInscription> getEnfants();
	
	public abstract AbstractInscription getParent();
	
	public abstract int getCoefficient();
	
	public abstract Double getNoteSeuil();
	
	public abstract boolean isTermine();
	
	public abstract boolean isAcquis();
	
	public abstract boolean isRattrapable();
	
	public abstract Calculateur getCalculateur();
	
}
