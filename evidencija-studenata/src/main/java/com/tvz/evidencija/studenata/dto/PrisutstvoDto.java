package com.tvz.evidencija.studenata.dto;

public class PrisutstvoDto {

	private int brojVjezbe;
	
	private int studentId;

	public PrisutstvoDto() {
		
	}
	
	public PrisutstvoDto(int brojVjezbe, int studentId) {
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
