package com.mygrades.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.InscriptionModule;

@Repository
public interface InscriptionModuleRepository extends CrudRepository<InscriptionModule, Long> {
	
}
