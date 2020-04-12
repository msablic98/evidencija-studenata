package com.tvz.evidencija.studenata.service;

import java.util.List;

import com.tvz.evidencija.studenata.entity.Prisustvo;

/**
 * 
 * @author msablic
 *
 * Service interface-i su "blueprint" implementacije servisa u kojoj pišemo funkcionalnost
 * svake navedene metode iz interface-a.
 * 
 * Jedan interface može imati više implementacija.
 * 
 * Ovaj interface sadrži metode koje su nam potrebne za rad sa klasom Prisustvo.
 * 
 */

public interface PrisustvoService {

	public Prisustvo dohvatiPrisustvoPoBrojuVjezbe(int brojVjezbe);
	
	public List<Prisustvo> dohvatiSve();
	
	public void spremi(Prisustvo prisustvo);
	
	public void obrisiSve();
	
}
