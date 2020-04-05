package com.tvz.evidencija.studenata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tvz.evidencija.studenata.dto.PrisutstvoDto;
import com.tvz.evidencija.studenata.dto.UpisOcjeneDto;
import com.tvz.evidencija.studenata.entity.Prisutstvo;
import com.tvz.evidencija.studenata.entity.Student;
import com.tvz.evidencija.studenata.excel.IzvozExcel;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private PrisutstvoService prisutstvoService;
	
	@GetMapping("/lista")
	public String getLista(Model model) {
		LOGGER.debug("Zahtjev za dohvaćanjem liste studenata");
		
		/**
			Nije potreban try/catch jer u slučaju da zapisa nema vraća se null.
		**/
		List<Student> studenti = studentService.findAll();
		
		model.addAttribute("studenti", studenti);
		
		return "studenti/studenti-lista";
	}
	
	@GetMapping("/formaZaDodavanje")
	public String formaZaDodavanje(Model model) {
		LOGGER.debug("Zahtjev za unošenje novog studenta");
		
		Student student = new Student();
		
		model.addAttribute("student", student);
		
		return "studenti/studenti-forma";
	}
	
	@PostMapping("/spremiStudenta")
	public String spremiStudenta(@ModelAttribute("student") Student student) {
		LOGGER.debug("Zahtjev za spremanje studenta: ", student.getIme() + student.getPrezime());
		
		/**
			Nije potreban try/catch jer se na frontendu vrši provjera unesenih podataka.
		**/
		studentService.save(student);
		
		return "redirect:/studenti/lista";
	}
	
	@GetMapping("/formaZaAzuriranje")
	public String formaZaAzuriranje(@RequestParam("studentId") int id, Model model) {
		LOGGER.debug("Zahtjev za ažuriranje studenta sa ID-jem: ", id);
		
		Student student = new Student();
		
		try {
			student = studentService.getStudentById(id);
		} catch (RuntimeException e) {
			LOGGER.error("Iznimka u dohvaćanju studenta sa ID-jem: " + id, e.getMessage());
		}
		
		model.addAttribute("student", student);
		
		return "studenti/studenti-forma";
	}
	
	@GetMapping("/obrisiStudenta")
	public String obrisiStudenta(@RequestParam("studentId") int id) {
		LOGGER.debug("Zahtjev za brisanje studenta sa ID-jem: ", id);
		
		Student student = studentService.getStudentById(id);
		
		if(student.isEvidentiran()) {
			// Ništa se ne događa jer je za studenta već prijavljena pristunost i ne može se obrisati.
		} else {
			studentService.deleteById(id);
		}
		
		return "redirect:/studenti/lista";
	}
	
	@GetMapping("/upisiPristustvo")
	public String upisiPrisutstvo(Model model) {
		LOGGER.debug("Zahtjev za upis pristustva");
		
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
		LOGGER.debug("Zahtjev za upis prisutstva za studenta sa ID-jem: ", prisutstvoDto.getStudentId());
		
		Prisutstvo prisutstvo = new Prisutstvo();
		
		if(prisutstvoDto.getStudentId() != 0) {
			try {
				Prisutstvo postojecePrisutstvo = prisutstvoService.getPrisutstvoByBrojVjezbe(prisutstvoDto.getBrojVjezbe());
				Student student = studentService.getStudentById(prisutstvoDto.getStudentId());
				
				if(postojecePrisutstvo != null) {
					if(postojecePrisutstvo.getStudenti().contains(studentService.getStudentById(prisutstvoDto.getStudentId()))) {
						// Ništa se ne događa jer ako student postoji u klasi za određenu vježbu, ne dodaje se ponovno.
					} else {
						postojecePrisutstvo.getStudenti().add(studentService.getStudentById(prisutstvoDto.getStudentId()));
						prisutstvo = postojecePrisutstvo;
					}
				} else {
					prisutstvo.setBrojVjezbe(prisutstvoDto.getBrojVjezbe());
					prisutstvo.setStudenti(List.of(studentService.getStudentById(prisutstvoDto.getStudentId())));
					student.setEvidentiran(true);
				}
			} catch (Exception e) {
				LOGGER.error("Iznimka u upisu prisutstva za studenta sa ID-jem: " + prisutstvoDto.getStudentId(), e.getMessage());
			}
		} else {
			prisutstvo.setBrojVjezbe(prisutstvoDto.getBrojVjezbe());
		}
		
		prisutstvoService.save(prisutstvo);
		
		return "redirect:/studenti/upisiPristustvo";
	}
	
	@GetMapping("/upisiOcjenu")
	public String upisiOcjenu(Model model) {
		LOGGER.debug("Zahtjev za upis ocjene");
		
		List<Prisutstvo> prisutstva = prisutstvoService.findAll();
		UpisOcjeneDto upisOcjeneDto = new UpisOcjeneDto();
		
		HashMap<Integer,List<Student>> studenti = new HashMap<>();
		HashMap<Student,Integer> brojDolazaka = new HashMap<>();
		List<Student> studentiZaUpis = new ArrayList<>();
		
		List<Student> sviStudenti = studentService.findAll();
		
		/**
			Iteriramo kroz postojeća prisutstva i punimo hashmapu studenti sa brojem vježbe kao ključ.
		**/
		if(prisutstva.size() == 10) {
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
					if(brojDolazaka.get(sviStudenti.get(i)) >= 2) {
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
		LOGGER.debug("Zahtjev za spremanje ocjene za studenta sa ID-jem: ", upisOcjeneDto.getStudentId());
		
		try {
			Student student = studentService.getStudentById(upisOcjeneDto.getStudentId());
			student.setOcjena(upisOcjeneDto.getOcjena());
			studentService.save(student);
		} catch (Exception e) {
			LOGGER.error("Iznimka u upisu ocjene za studenta sa ID-jem: " + upisOcjeneDto.getStudentId(), e.getMessage());
		}
		
		return "redirect:/studenti/upisiOcjenu";
	}
	
	@GetMapping("/pregledOcjena")
	public String pregledOcjena(Model model) {
		LOGGER.debug("Zahtjev za pregled ocjena studenata");
		
		/**
		   Provjera ako su unesene sve vježbe, ako jesu prikazati ćemo tablicu studenata.
		   Ako nisu, pokazuje se poruka da se moraju evidentirati sve vježbe i ocjene.
		**/
		boolean uneseneSveVjezbe = false;
		List<Prisutstvo> prisutstva = prisutstvoService.findAll();
		if(prisutstva.size() == 10) {
			uneseneSveVjezbe = true;
		}
		
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
			LOGGER.error("Iznimka u dohvatu pregleda ocjena", e.getMessage());
		}
		
		model.addAttribute("uspjesniStudenti", uspjesniStudenti);
		model.addAttribute("neuspjesniStudenti", neuspjesniStudenti);
		model.addAttribute("uneseneSveVjezbe", uneseneSveVjezbe);
		
		return "studenti/studenti-pregled";
	}
	
	@GetMapping("/izveziUspjesne")
	public ModelAndView izveziUspjesne(){
		
        List<Student> studentiZaIzvoz = new ArrayList<Student>(); 
        
        try {
			List<Student> studenti = studentService.findAll();
			/**
			 	Iteriramo kroz listu svih studenata te ako je studentu ocjena 0 (nije unesena) ili 1 (nedovoljan) ne izvozi se u Excel.
			**/	
			for(int i = 0; i < studenti.size(); i++) {
				if(!(Integer.valueOf(studenti.get(i).getOcjena()).equals(Integer.valueOf(0)) || Integer.valueOf(studenti.get(i).getOcjena()).equals(Integer.valueOf(1)))){
					studentiZaIzvoz.add(studenti.get(i));
				} 
			}
		} catch (Exception e) {
			LOGGER.error("Iznimka u dohvatu studenata za izvoz u Excel dokument", e.getMessage());
		}
        
        if(studentiZaIzvoz.isEmpty()) {
        	return null;
        }
        
        return new ModelAndView(new IzvozExcel(), "studentiZaIzvoz", studentiZaIzvoz);
	}
	
	@GetMapping("/izveziNeuspjesne")
	public ModelAndView izveziNespjesne(){
		
        List<Student> studentiZaIzvoz = new ArrayList<Student>(); 
        
        try {
			List<Student> studenti = studentService.findAll();
			/**
			 	Iteriramo kroz listu svih studenata te ako je studentu ocjena 0 (nije unesena) ili 1 (nedovoljan) izvozi se u Excel.
			**/
			for(int i = 0; i < studenti.size(); i++) {
				if(Integer.valueOf(studenti.get(i).getOcjena()).equals(Integer.valueOf(0)) || Integer.valueOf(studenti.get(i).getOcjena()).equals(Integer.valueOf(1))){
					studentiZaIzvoz.add(studenti.get(i));
				} 
			}
		} catch (Exception e) {
			LOGGER.error("Iznimka u dohvatu studenata za izvoz u Excel dokument", e.getMessage());
		}
        
        if(studentiZaIzvoz.isEmpty()) {
        	return null;
        }
        
        return new ModelAndView(new IzvozExcel(), "studentiZaIzvoz", studentiZaIzvoz);
	}
	
}
