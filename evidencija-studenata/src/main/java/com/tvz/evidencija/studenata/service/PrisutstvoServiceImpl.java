package com.tvz.evidencija.studenata.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tvz.evidencija.studenata.dao.PrisutstvoRepository;
import com.tvz.evidencija.studenata.entity.Prisutstvo;

/**
 * 
 * @author msablic
 *
 * Implementacijska klasa sa @Service anotacijom sadrži logiku svih metoda koje su navedene u interface klasi.
 * 
 * Svaka metoda za koju pišemo logiku, mora sadržavati @Override anotaciju koja daje do znanja Spring-u kako se
 * radi o implementaciji postojeće metode.
 * 
 * Ovdje također koristimo Repository klasu za sve ORM akcije. Sva polja koja su označena sa @Autowired anotacijom 
 * učitavaju se u klasi pomoću dependecy injection-a. 
 * 
 * Ova klasa sadrži implementaciju svih metoda za manipuliranje Prisutstvo podacima.
 * 
 */

@Service
public class PrisutstvoServiceImpl implements PrisutstvoService {

	@Autowired
	private PrisutstvoRepository prisutstvoRepository;
	
	@Override
	public List<Prisutstvo> dohvatiSve() {

		return prisutstvoRepository.findAll();
	}

	@Override
	public void spremi(Prisutstvo prisutstvo) {

		prisutstvoRepository.save(prisutstvo);
	}
	
	@Override
	public Prisutstvo dohvatiPrisutstvoPoBrojuVjezbe(int brojVjezbe) {
		
		List<Prisutstvo> prisutstva =  prisutstvoRepository.findAll();
		Prisutstvo valjanoPrisutstvo = new Prisutstvo();
		
		if(!prisutstva.isEmpty()) {
			/**
			    Iteriramo kroz listu dohvaćenih prisutstava te ako broj vježbe prisutstva odgovara broju vježbe
			    koji smo poslali kao parametar dodaje se u listu valjanoPrisutstvo.
			   	Ako ni jedno prisutstvo ne odgovara parametru, vraća se null.
			**/
			for(int i = 0; i < prisutstva.size(); i++) {
				if(prisutstva.get(i).getBrojVjezbe() == brojVjezbe) {
					valjanoPrisutstvo = prisutstva.get(i);
				} else {
					valjanoPrisutstvo = null;
				}
			}
		} else {
			valjanoPrisutstvo = null;
		}
		
		return valjanoPrisutstvo;
	}
	
	@Override
	public void obrisiSve() {
		
		prisutstvoRepository.deleteAll();
		
	}

}
