package com.mygrades.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mygrades.domain.Etudiant;

@Repository
public interface EtudiantRepository extends CrudRepository<Etudiant, Long> {

}
