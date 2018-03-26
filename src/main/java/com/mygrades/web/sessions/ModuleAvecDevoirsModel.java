package com.mygrades.web.sessions;

import java.util.ArrayList;
import java.util.List;

public class ModuleAvecDevoirsModel {
	
	private Long id;
	private String code;
	private String nom;
	private List<DevoirModel> devoirs = new ArrayList<>();
	
	
	public ModuleAvecDevoirsModel(Long idModule, String codeModule, String nomModule) {
		this.id = idModule;
		this.code = codeModule;
		this.nom = nomModule;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public List<DevoirModel> getDevoirs() {
		return devoirs;
	}
	public void setDevoirs(List<DevoirModel> devoirs) {
		this.devoirs = devoirs;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
