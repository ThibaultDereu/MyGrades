package com.mygrades.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.InscriptionDevoir;

@Repository
public interface InscriptionDevoirRepository extends CrudRepository<InscriptionDevoir, Long> {
	
}
