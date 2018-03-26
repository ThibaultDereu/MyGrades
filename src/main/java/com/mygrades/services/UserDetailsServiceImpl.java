package com.mygrades.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mygrades.domain.Utilisateur;
import com.mygrades.repositories.UtilisateurRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UtilisateurRepository repUtilisateur;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Utilisateur utilisateur = repUtilisateur.findByEmail(email);	
		
		if (utilisateur == null) {
			throw new UsernameNotFoundException("Utilisateur inconnu.");
		}
		
		return utilisateur;
	}
	
}
