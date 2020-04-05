package com.tvz.evidencija.studenata.dto;

/**
 * 
 * @author msablic
 *
 * DTO ili Data Transfer Object klase su klase koje služe za prenošenje podataka između procesa.
 * 
 * U ovom slučaju, služi za prenošenje ocjene koje se upisuje za studenta koji se prenosi pomoću ID-a.
 */

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
