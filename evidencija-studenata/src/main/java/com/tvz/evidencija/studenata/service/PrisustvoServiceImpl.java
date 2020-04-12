package com.tvz.evidencija.studenata.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tvz.evidencija.studenata.dao.PrisustvoRepository;
import com.tvz.evidencija.studenata.entity.Prisustvo;

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
 * Ova klasa sadrži implementaciju svih metoda za manipuliranje Prisustvo podacima.
 * 
 */

@Service
public class PrisustvoServiceImpl implements PrisustvoService {

	@Autowired
	private PrisustvoRepository prisustvoRepository;
	
	@Override
	public List<Prisustvo> dohvatiSve() {

		return prisustvoRepository.findAll();
	}

	@Override
	public void spremi(Prisustvo prisutstvo) {

		prisustvoRepository.save(prisutstvo);
	}
	
	@Override
	public Prisustvo dohvatiPrisustvoPoBrojuVjezbe(int brojVjezbe) {
		
		List<Prisustvo> prisustva =  prisustvoRepository.findAll();
		Prisustvo valjanoPrisustvo = new Prisustvo();
		
		if(!prisustva.isEmpty()) {
			/**
			    Iteriramo kroz listu dohvaćenih prisustava te ako broj vježbe prisustva odgovara broju vježbe
			    koji smo poslali kao parametar dodaje se u listu valjanoPrisustvo.
			   	Ako ni jedno prisustvo ne odgovara parametru, vraća se null.
			**/
			for(int i = 0; i < prisustva.size(); i++) {
				if(prisustva.get(i).getBrojVjezbe() == brojVjezbe) {
					valjanoPrisustvo = prisustva.get(i);
				} else {
					valjanoPrisustvo = null;
				}
			}
		} else {
			valjanoPrisustvo = null;
		}
		
		return valjanoPrisustvo;
	}
	
	@Override
	public void obrisiSve() {
		
		prisustvoRepository.deleteAll();
		
	}

}
