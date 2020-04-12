package com.tvz.evidencija.studenata.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tvz.evidencija.studenata.entity.Prisustvo;

/**
 * 
 * @author msablic
 *
 * DAO sloj odnosno repository sloj služi za izvršavanje upita na bazu podataka.
 * 
 * Spring data JPA repository sadrži API za CRUD operacije.
 * 
 */

public interface PrisustvoRepository extends JpaRepository<Prisustvo, Integer> {
	
}
