package com.tvz.evidencija.studenata.dto;

public class UpisOcjeneDto {

	private int studentId;
	
	private int ocjena;

	public UpisOcjeneDto() {
		
	}
	
	public UpisOcjeneDto(int studentId, int ocjena) {
		super();
		this.studentId = studentId;
		this.ocjena = ocjena;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getOcjena() {
		return ocjena;
	}

	public void setOcjena(int ocjena) {
		this.ocjena = ocjena;
	}
	
	
}
