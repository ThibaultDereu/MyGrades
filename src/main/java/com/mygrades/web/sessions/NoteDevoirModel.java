package com.mygrades.web.sessions;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

public class NoteDevoirModel {

	private Long idInscriptionDevoir;
	
	@NotNull
	@Range(min=0, max=20)
	private Double note;

	public Long getIdInscriptionDevoir() {
		return idInscriptionDevoir;
	}

	public void setIdInscriptionDevoir(Long idInscriptionDevoir) {
		this.idInscriptionDevoir = idInscriptionDevoir;
	}

	public Double getNote() {
		return note;
	}

	public void setNote(Double note) {
		this.note = note;
	}
	
}
