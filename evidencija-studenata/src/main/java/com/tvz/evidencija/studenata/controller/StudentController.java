package com.tvz.evidencija.studenata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tvz.evidencija.studenata.dto.PrisutstvoDto;
import com.tvz.evidencija.studenata.dto.UpisOcjeneDto;
import com.tvz.evidencija.studenata.entity.Prisutstvo;
import com.tvz.evidencija.studenata.entity.Student;
import com.tvz.evidencija.studenata.service.PrisutstvoService;
import com.tvz.evidencija.studenata.service.StudentService;

/**
 * 
 * @author msablic
 *
 * Controller klase sadrže logiku aplikacije te su odgovorne za tok procesa aplikacije. 
 * 
 * U našem slučaju, StudentController klasa odgovorna je za sve upite koji se šalju na /student endpoint.
 * 
 * Ovisno o nastavku endpoint-a, ( /lista, /spremiStudenta...) odvija se logika mapirana na taj endpoint.
 * 
 */

@Controller
@RequestMapping("/studenti")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private PrisutstvoService prisutstvoService;
	
	@GetMapping("/lista")
	public String getLista(Model model) {
		
		/**
			Nije potreban try/catch jer u slučaju da zapisa nema vraća se null.
		**/
		List<Student> studenti = studentService.findAll();
		
		model.addAttribute("studenti", studenti);
		
		return "studenti/studenti-lista";
	}
	
	@GetMapping("/formaZaDodavanje")
	public String formaZaDodavanje(Model model) {
		
		Student student = new Student();
		
		model.addAttribute("student", student);
		
		return "studenti/studenti-forma";
	}
	
	@PostMapping("/spremiStudenta")
	public String spremiStudenta(@ModelAttribute("student") Student student) {
		
		/**
			Nije potreban try/catch jer se na frontendu vrši provjera unesenih podataka.
		**/
		studentService.save(student);
		
		return "redirect:/studenti/lista";
	}
	
	@GetMapping("/formaZaAzuriranje")
	public String formaZaAzuriranje(@RequestParam("studentId") int id, Model model) {
		
		Student student = new Student();
		
		try {
			student = studentService.getStudentById(id);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
		
		model.addAttribute("student", student);
		
		return "studenti/studenti-forma";
	}
	
	@GetMapping("/obrisiStudenta")
	public String obrisiStudenta(@RequestParam("studentId") int id) {
		
		/**
			Nije potreban try/catch jer u slučaju da zapis ne postoji ignorira se.
		**/
		studentService.deleteById(id);
		
		return "redirect:/studenti/lista";
	}
	
	@GetMapping("/upisiPristustvo")
	public String upisiPrisutstvo(Model model) {
		
		/**
			Nije potreban try/catch jer u slučaju da zapisa nema vraća se null.
		**/
		List<Student> studenti = studentService.findAll();
		PrisutstvoDto prisutstvoDto = new PrisutstvoDto();
		
		
		model.addAttribute("studenti", studenti);
		model.addAttribute("prisutstvoDto",prisutstvoDto);
		
		return "studenti/studenti-prisutstvo";
		
	}
	
	@PostMapping("/spremiPrisutstvo")
	public String spremiPrisutstvo(@ModelAttribute("prisutstvoDto") PrisutstvoDto prisutstvoDto) {
		
		try {
			Prisutstvo prisutstvo = new Prisutstvo();
			Prisutstvo postojecePrisutstvo = prisutstvoService.getPrisutstvoByBrojVjezbe(prisutstvoDto.getBrojVjezbe());
			
			if(postojecePrisutstvo != null) {
				if(postojecePrisutstvo.getStudenti().contains(studentService.getStudentById(prisutstvoDto.getStudentId()))) {
					// Ništa se ne događa jer ako student postoji u klasi za određenu vježbu, ne dodaje se ponovno.
				} else {
					postojecePrisutstvo.getStudenti().add(studentService.getStudentById(prisutstvoDto.getStudentId()));
					prisutstvo = postojecePrisutstvo;
				}
			} else {
				prisutstvo = new Prisutstvo();
				
				prisutstvo.setBrojVjezbe(prisutstvoDto.getBrojVjezbe());
				prisutstvo.setStudenti(List.of(studentService.getStudentById(prisutstvoDto.getStudentId())));
			}
			
			prisutstvoService.save(prisutstvo);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "redirect:/studenti/upisiPristustvo";
	}
	
	@GetMapping("/upisiOcjenu")
	public String upisiOcjenu(Model model) {
		
		List<Prisutstvo> prisutstva = prisutstvoService.findAll();
		UpisOcjeneDto upisOcjeneDto = new UpisOcjeneDto();
		
		HashMap<Integer,List<Student>> studenti = new HashMap<>();
		HashMap<Student,Integer> brojDolazaka = new HashMap<>();
		List<Student> studentiZaUpis = new ArrayList<>();
		
		List<Student> sviStudenti = studentService.findAll();
		
		/**
			Iteriramo kroz postojeća prisutstva i punimo hashmapu studenti sa brojem vježbe kao ključ.
		**/
		if(prisutstva.size() == 3) {
			for(int i=0; i<prisutstva.size(); i++) {
				studenti.put(i,prisutstva.get(i).getStudenti());
			}
			
			/** 
			    Iteriramo kroz napunjenu hashmapu studenti i svakoga studenta u mapi uspoređujemo sa svim dostupnim studentima.
			 	Ako je student pronađen, provjerava se je li već dodan u mapu brojDolazaka koja prati broj dolazaka studenata.
				Ako je već dodan, counter se povećava za jedan i dodaje u mapu za tog studenta.
				Ako nije dodan, kreira se novi zapis u mapi i dodaje mu se broj jednog dolaska.
			**/
			for(int i = 0; i < studenti.size(); i++) {
				List<Student> temp = studenti.get(i);
				for(int j = 0; j < sviStudenti.size(); j++) {
					if(temp.contains(sviStudenti.get(j))) {
						if(brojDolazaka.containsKey(sviStudenti.get(j))) {
							int increment = brojDolazaka.get(sviStudenti.get(j)) + 1;
							brojDolazaka.put(sviStudenti.get(j), increment);
						} else {
							brojDolazaka.put(sviStudenti.get(j), 1);
						}
					}
				}
			}

			/** 
				Iteriramo kroz listu svih dohvaćenih studenata te ako je neki student pronađen u mapi brojDolazaka
				provjerava se je li broj dolazaka tog studenta veći ili jednak 6 od 10. 
				Ako je dodaje se u mapu studentiZaUpis koja se vraća na upis ocjene.
				Ako nije, odbacuje se.
			**/
			for(int i = 0; i < sviStudenti.size(); i++) {
				if(brojDolazaka.get(sviStudenti.get(i)) != null) {
					if(brojDolazaka.get(sviStudenti.get(i)) >= 6) {
						studentiZaUpis.add(sviStudenti.get(i));
					}
				}
			}
		}
		
		model.addAttribute("studenti", studentiZaUpis);
		model.addAttribute("upisOcjeneDto", upisOcjeneDto);
		
		return "studenti/studenti-ocjene";
	}
	
	@PostMapping("/spremiOcjenu")
	public String spremiOcjenu(@ModelAttribute("upisOcjeneDto") UpisOcjeneDto upisOcjeneDto) {
		
		try {
			Student student = studentService.getStudentById(upisOcjeneDto.getStudentId());
			student.setOcjena(upisOcjeneDto.getOcjena());
			studentService.save(student);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "redirect:/studenti/upisiOcjenu";
	}
	
	@GetMapping("/pregledOcjena")
	public String pregledOcjena(Model model) {
		
		List<Student> uspjesniStudenti = new ArrayList<>();
		List<Student> neuspjesniStudenti = new ArrayList<>();
		
		try {
			List<Student> studenti = studentService.findAll();
			/**
			 	Iteriramo kroz listu svih studenata te ako je studentu ocjena 0 (nije unesena) ili 1 (nedovoljan) dodaje se u mapu neuspjesniStudenti.
			 	U protivnom, student se dodaje u listu uspjesniStudenti.
			**/
			for(int i = 0; i < studenti.size(); i++) {
				// int vrijednosti pretvaramo u Integer vrijednosti kako bi usporedbu vršili pomoću Java metode .equals
				if(Integer.valueOf(studenti.get(i).getOcjena()).equals(Integer.valueOf(0)) || Integer.valueOf(studenti.get(i).getOcjena()).equals(Integer.valueOf(1))){
					neuspjesniStudenti.add(studenti.get(i));
				} else {
					uspjesniStudenti.add(studenti.get(i));
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		model.addAttribute("uspjesniStudenti", uspjesniStudenti);
		model.addAttribute("neuspjesniStudenti", neuspjesniStudenti);
		
		return "studenti/studenti-pregled";
	}
	
}
