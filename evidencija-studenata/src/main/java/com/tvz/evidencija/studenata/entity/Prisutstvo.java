package com.tvz.evidencija.studenata.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 
 * @author msablic
 *
 * Entity klase odnosno entiteti su POJO (Plain Old Java Object) klase koje imaju sposobnost reprezentacije
 * objekata u bazi podataka.
 * 
 * Ovaj entitet sadrži podatke za tablicu "prisutstvo" te sve njezine redove. 
 * 
 */

@Entity
@Table(name="prisutstvo")
public class Prisutstvo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Student> studenti;
	
	@Column(name="broj_vjezbe")
	private int brojVjezbe;

	public Prisutstvo() {
		
	}
	
	public Prisutstvo(int id, List<Student> studenti, int brojVjezbe) {
		super();
		this.id = id;
		this.studenti = studenti;
		this.brojVjezbe = brojVjezbe;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Student> getStudenti() {
		return studenti;
	}

	public void setStudenti(List<Student> studenti) {
		this.studenti = studenti;
	}

	public int getBrojVjezbe() {
		return brojVjezbe;
	}

	public void setBrojVjezbe(int brojVjezbe) {
		this.brojVjezbe = brojVjezbe;
	}

	@Override
	public String toString() {
		return "Prisutstvo [id=" + id + ", studenti=" + studenti+ ", brojVjezbe="
				+ brojVjezbe + "]";
	}
	
}
