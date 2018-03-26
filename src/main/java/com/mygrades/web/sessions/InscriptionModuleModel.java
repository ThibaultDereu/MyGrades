package com.mygrades.web.sessions;

import java.util.ArrayList;
import java.util.List;

public class InscriptionModuleModel {

	private Long id;
	private String codeModule;
	private String nomModule;
	private Integer coefficient;
	private Boolean rattrapable;
	private Double noteSeuil;
	private String statut;
	private Double note;
	private List<InscriptionDevoirModel> inscriptionsDevoirs = new ArrayList<>();

	public InscriptionModuleModel() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodeModule() {
		return codeModule;
	}

	public void setCodeModule(String codeModule) {
		this.codeModule = codeModule;
	}

	public String getNomModule() {
		return nomModule;
	}

	public void setNomModule(String nomModule) {
		this.nomModule = nomModule;
	}

	public Integer getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Integer coeff) {
		this.coefficient = coeff;
	}

	public Boolean getRattrapable() {
		return rattrapable;
	}

	public void setRattrapable(Boolean rattrapable) {
		this.rattrapable = rattrapable;
	}

	public Double getNoteSeuil() {
		return noteSeuil;
	}

	public void setNoteSeuil(Double noteSeuil) {
		this.noteSeuil = noteSeuil;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Double getNote() {
		return note;
	}

	public void setNote(Double note) {
		this.note = note;
	}

	public List<InscriptionDevoirModel> getInscriptionsDevoirs() {
		return inscriptionsDevoirs;
	}

	public void setInscriptionsDevoirs(List<InscriptionDevoirModel> inscriptionsDevoirs) {
		this.inscriptionsDevoirs = inscriptionsDevoirs;
	}

}
