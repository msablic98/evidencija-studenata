package com.tvz.evidencija.studenata.dto;

/**
 * 
 * @author msablic
 *
 * DTO ili Data Transfer Object klase su klase koje služe za prenošenje podataka između procesa.
 * 
 * U ovom slučaju, služi za prenošenje broja vježbe za koju se upisuje prisustvo studenta koji se prenosi pomoću ID-a.
 */

public class PrisustvoDto {

	private int brojVjezbe;
	
	private int studentId;

	public PrisustvoDto() {
		
	}
	
	public PrisustvoDto(int brojVjezbe, int studentId) {
		super();
		this.brojVjezbe = brojVjezbe;
		this.studentId = studentId;
	}

	public int getBrojVjezbe() {
		return brojVjezbe;
	}

	public void setBrojVjezbe(int brojVjezbe) {
		this.brojVjezbe = brojVjezbe;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	
	
}
