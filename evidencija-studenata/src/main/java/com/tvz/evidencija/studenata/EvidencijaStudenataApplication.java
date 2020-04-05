package com.tvz.evidencija.studenata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * @author msablic
 *
 * Main klasa koja pokreće aplikaciju označena je sa @SpringBootApplication anotacijom koja označava 
 * glavnu klasu.
 * 
 */

@SpringBootApplication
public class EvidencijaStudenataApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvidencijaStudenataApplication.class, args);
	}

}
