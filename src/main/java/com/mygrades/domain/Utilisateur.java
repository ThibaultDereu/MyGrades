package com.mygrades.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@NamedEntityGraph(name = "graphUtilisateur", attributeNodes = { @NamedAttributeNode("profilEtudiant"),
		@NamedAttributeNode("profilEnseignant") })
public class Utilisateur implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String email;
	private String password;
	@NotNull
	private String prenom;
	@NotNull
	private String nom;
	private Boolean admin;

	@OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Etudiant profilEtudiant;

	@OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Enseignant profilEnseignant;

	protected Utilisateur() {
	}

	public Utilisateur(String email, String prenom, String nom) {
		this.email = email;
		this.prenom = prenom;
		this.nom = nom;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Boolean isAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Etudiant getProfilEtudiant() {
		return profilEtudiant;
	}

	public void setProfilEtudiant(Etudiant profilEtudiant) {
		this.profilEtudiant = profilEtudiant;
	}

	public Enseignant getProfilEnseignant() {
		return profilEnseignant;
	}

	public void setProfilEnseignant(Enseignant profilEnseignant) {
		this.profilEnseignant = profilEnseignant;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<GrantedAuthority> authorities = new HashSet<>();

		if (this.admin) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		if (this.profilEtudiant != null) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ETUDIANT"));
		}

		if (this.profilEnseignant != null) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ENSEIGNANT"));
		}

		return authorities;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
