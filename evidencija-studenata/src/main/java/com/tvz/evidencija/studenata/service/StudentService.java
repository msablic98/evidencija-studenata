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

	public List<Student> findAll();
	
	public Student getStudentById(int id);
	
	public void save(Student student);
	
	public void deleteById(int id);
	
}
