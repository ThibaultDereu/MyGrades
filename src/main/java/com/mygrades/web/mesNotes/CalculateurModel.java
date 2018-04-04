package com.mygrades.web.mesNotes;

public class CalculateurModel {
	
	private Long id;
	private Double noteReelle;
	private Double noteObjectif;
	private Double noteMin;
	private Double noteMax;
	private Boolean acquisReel;

	public CalculateurModel() {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getNoteReelle() {
		return noteReelle;
	}

	public void setNoteReelle(Double noteReelle) {
		this.noteReelle = noteReelle;
	}

	public Double getNoteObjectif() {
		return noteObjectif;
	}

	public void setNoteObjectif(Double noteObjectif) {
		this.noteObjectif = noteObjectif;
	}

	public Double getNoteMin() {
		return noteMin;
	}

	public void setNoteMin(Double noteMin) {
		this.noteMin = noteMin;
	}

	public Double getNoteMax() {
		return noteMax;
	}

	public void setNoteMax(Double noteMax) {
		this.noteMax = noteMax;
	}

	public Boolean getAcquisReel() {
		return acquisReel;
	}

	public void setAcquisReel(Boolean acquisReel) {
		this.acquisReel = acquisReel;
	}
	
}
