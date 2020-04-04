package com.tvz.evidencija.studenata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="student")
public class Student {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="ime")
	private String ime;
	
	@Column(name="prezime")
	private String prezime;
	
	@Column(name="email")
	private String email;
	
	@Column(name="jmbag")
	private String jmbag;
	
	@Column(name="ocjena")
	private int ocjena;
	
	public Student() {
		
	}

	public Student(int id, String ime, String prezime, String email, String jmbag) {
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.jmbag = jmbag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

	public String getJmbag() {
		return jmbag;
	}

	public void setJmbag(String jmbag) {
		this.jmbag = jmbag;
	}
	

	public int getOcjena() {
		return ocjena;
	}

	public void setOcjena(int ocjena) {
		this.ocjena = ocjena;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", ime=" + ime + ", prezime=" + prezime + ", email=" + email + ", jmbag=" + jmbag
				+ "]";
	}

}
