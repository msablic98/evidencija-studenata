package com.tvz.evidencija.studenata.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tvz.evidencija.studenata.entity.Student;

/**
 * 
 * @author msablic
 *
 * DAO sloj odnosno repository sloj služi za izvršavanje Hibernate upita.
 * 
 * Hibernate koristi Springov JPA repository koji sadrži API za CRUD operacije.
 */

public interface StudentRepository extends JpaRepository<Student, Integer> {
	
}
