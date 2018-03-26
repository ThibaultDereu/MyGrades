package com.mygrades.web.administration;

public class ModuleModelSimple {

	private Long id;
	private String code;
	private String nom;

	public ModuleModelSimple() {
	}

	public ModuleModelSimple(Long id, String code, String nom) {
		this.id = id;
		this.code = code;
		this.nom = nom;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
}
