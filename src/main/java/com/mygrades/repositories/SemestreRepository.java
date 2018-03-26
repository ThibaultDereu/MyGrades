package com.mygrades.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.Semestre;

@Repository
public interface SemestreRepository extends CrudRepository<Semestre, Long> {}
