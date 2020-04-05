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

	public Prisutstvo dohvatiPrisutstvoPoBrojuVjezbe(int brojVjezbe);
	
	public List<Prisutstvo> dohvatiSve();
	
	public void spremi(Prisutstvo prisutstvo);
	
	public void obrisiSve();
	
}
