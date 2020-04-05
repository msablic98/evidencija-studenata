package com.tvz.evidencija.studenata.service;

import java.util.List;

import com.tvz.evidencija.studenata.entity.Prisutstvo;

/**
 * 
 * @author msablic
 *
 * Service interface-i su "blueprint" implementacije servisa u kojoj pišemo funkcionalnost
 * svake navedene metode iz interface-a.
 * 
 * Jedan interface može imati više implementacija.
 * 
 * Ovaj interface sadrži metode koje su nam potrebne za rad sa klasom Prisutstvo.
 * 
 */

public interface PrisutstvoService {

	public Prisutstvo getPrisutstvoByBrojVjezbe(int brojVjezbe);
	
	public List<Prisutstvo> findAll();
	
	public void save(Prisutstvo prisutstvo);
	
}
