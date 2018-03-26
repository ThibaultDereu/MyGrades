package com.mygrades.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.Devoir;

@Repository
public interface DevoirRepository extends CrudRepository<Devoir, Long> {
	
}
