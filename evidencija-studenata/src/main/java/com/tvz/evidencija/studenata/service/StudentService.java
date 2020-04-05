package com.tvz.evidencija.studenata.service;

import java.util.List;

import com.tvz.evidencija.studenata.entity.Student;

/**
 * 
 * @author msablic
 *
 * Service interface-i su "blueprint" implementacije servisa u kojoj pišemo funkcionalnost
 * svake navedene metode iz interface-a. 
 * 
 * Jedan interface može imati više implementacija.
 * 
 * Ovaj interface sadrži metode koje su nam potrebne za rad sa klasom Student.
 * 
 */


public interface StudentService {

	public List<Student> dohvatiSve();
	
	public Student dohvatiStudentaPoId(int id);
	
	public void spremi(Student student);
	
	public void obrisiStudentaPoId(int id);
	
	public void obrisiSve();
	
}
