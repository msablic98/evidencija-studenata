package com.tvz.evidencija.studenata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="prisutstvo")
public class Prisutstvo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="student_id")
	private int studentId;
	
	@Column(name="prisutan")
	private boolean prisutan;
	
	@Column(name="broj_vjezbe")
	private int brojVjezbe;

	public Prisutstvo() {
		
	}
	
	public Prisutstvo(int id, int studentId, boolean prisutan, int brojVjezbe) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.prisutan = prisutan;
		this.brojVjezbe = brojVjezbe;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public boolean isPrisutan() {
		return prisutan;
	}

	public void setPrisutan(boolean prisutan) {
		this.prisutan = prisutan;
	}

	public int getBrojVjezbe() {
		return brojVjezbe;
	}

	public void setBrojVjezbe(int brojVjezbe) {
		this.brojVjezbe = brojVjezbe;
	}
	
	
}
