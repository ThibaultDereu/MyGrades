package com.mygrades.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.InscriptionModule;
import com.mygrades.domain.InscriptionSession;

@Repository
public interface InscriptionModuleRepository extends CrudRepository<InscriptionModule, Long> {
	
}
