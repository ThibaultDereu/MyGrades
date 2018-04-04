package com.mygrades.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.Calculateur;

@Repository
public interface CalculateurRepository extends CrudRepository<Calculateur, Long> {
			
}
