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
	
	
}
