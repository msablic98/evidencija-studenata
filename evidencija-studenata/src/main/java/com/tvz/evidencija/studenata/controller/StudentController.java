package com.tvz.evidencija.studenata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tvz.evidencija.studenata.dto.PrisustvoDto;
import com.tvz.evidencija.studenata.dto.UpisOcjeneDto;
import com.tvz.evidencija.studenata.entity.Prisustvo;
import com.tvz.evidencija.studenata.entity.Student;
import com.tvz.evidencija.studenata.excel.IzvozExcel;
import com.tvz.evidencija.studenata.service.PrisustvoService;
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
	private PrisustvoService prisustvoService;
	
	@GetMapping("/lista")
	public String getLista(Model model) {
		LOGGER.debug("Zahtjev za dohvaćanjem liste studenata");
		
		/**
			Nije potreban try/catch jer u slučaju da zapisa nema vraća se null.
		**/
		List<Student> studenti = studentService.dohvatiSve();
		
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
		studentService.spremi(student);
		
		return "redirect:/studenti/lista";
	}
	
	@GetMapping("/formaZaAzuriranje")
	public String formaZaAzuriranje(@RequestParam("studentId") int id, Model model) {
		LOGGER.debug("Zahtjev za ažuriranje studenta sa ID-jem: ", id);
		
		Student student = new Student();
		
		try {
			student = studentService.dohvatiStudentaPoId(id);
		} catch (RuntimeException e) {
			LOGGER.error("Iznimka u dohvaćanju studenta sa ID-jem: " + id, e.getMessage());
		}
		
		model.addAttribute("student", student);
		
		return "studenti/studenti-forma";
	}
	
	@GetMapping("/obrisiStudenta")
	public String obrisiStudenta(@RequestParam("studentId") int id) {
		LOGGER.debug("Zahtjev za brisanje studenta sa ID-jem: ", id);
		
		Student student = studentService.dohvatiStudentaPoId(id);
		
		if(student.isEvidentiran()) {
			// Ništa se ne događa jer je za studenta već prijavljena prisutnost i ne može se obrisati.
		} else {
			studentService.obrisiStudentaPoId(id);
		}
		
		return "redirect:/studenti/lista";
	}
	
	@GetMapping("/upisiPrisustvo")
	public String upisiPrisustvo(Model model) {
		LOGGER.debug("Zahtjev za upis prisustva");
		
		/**
			Nije potreban try/catch jer u slučaju da zapisa nema vraća se null.
		**/
		List<Student> studenti = studentService.dohvatiSve();
		PrisustvoDto prisustvoDto = new PrisustvoDto();
		
		
		model.addAttribute("studenti", studenti);
		model.addAttribute("prisustvoDto",prisustvoDto);
		
		return "studenti/studenti-prisustvo";
		
	}
	
	@PostMapping("/spremiPrisustvo")
	public String spremiPrisustvo(@ModelAttribute("prisustvoDto") PrisustvoDto prisustvoDto) {
		LOGGER.debug("Zahtjev za upis prisutstva za studenta sa ID-jem: ", prisustvoDto.getStudentId());
		
		Prisustvo prisustvo = new Prisustvo();
		
		if(prisustvoDto.getStudentId() != 0) {
			try {
				Prisustvo postojecePrisustvo = prisustvoService.dohvatiPrisustvoPoBrojuVjezbe(prisustvoDto.getBrojVjezbe());
				Student student = studentService.dohvatiStudentaPoId(prisustvoDto.getStudentId());
				
				if(postojecePrisustvo != null) {
					if(postojecePrisustvo.getStudenti().contains(student)) {
						// Ništa se ne događa jer ako student postoji u klasi za određenu vježbu, ne dodaje se ponovno.
					} else {
						postojecePrisustvo.getStudenti().add(student);
						prisustvo = postojecePrisustvo;
						student.setEvidentiran(true);
					}
				} else {
					prisustvo.setBrojVjezbe(prisustvoDto.getBrojVjezbe());
					// Workaround kreiranja nove liste iz razloga što na serveru ne radi kada se student dodaje pomoću List.of(student)
					List<Student> temp = new ArrayList<>();
					temp.add(student);
					prisustvo.setStudenti(temp);
					student.setEvidentiran(true);
				}
			} catch (Exception e) {
				LOGGER.error("Iznimka u upisu prisustva za studenta sa ID-jem: " + prisustvoDto.getStudentId(), e.getMessage());
			}
		} else {
			prisustvo.setBrojVjezbe(prisustvoDto.getBrojVjezbe());
		}
		
		prisustvoService.spremi(prisustvo);
		
		return "redirect:/studenti/upisiPrisustvo";
	}
	
	@GetMapping("/upisiOcjenu")
	public String upisiOcjenu(Model model) {
		LOGGER.debug("Zahtjev za upis ocjene");
		
		UpisOcjeneDto upisOcjeneDto = new UpisOcjeneDto();
		
		List<Student> studentiZaUpis = dohvatiStudenteZaPotpis();
		
		model.addAttribute("studenti", studentiZaUpis);
		model.addAttribute("upisOcjeneDto", upisOcjeneDto);
		
		return "studenti/studenti-ocjene";
	}

	@PostMapping("/spremiOcjenu")
	public String spremiOcjenu(@ModelAttribute("upisOcjeneDto") UpisOcjeneDto upisOcjeneDto) {
		LOGGER.debug("Zahtjev za spremanje ocjene za studenta sa ID-jem: ", upisOcjeneDto.getStudentId());
		
		try {
			Student student = studentService.dohvatiStudentaPoId(upisOcjeneDto.getStudentId());
			student.setOcjena(upisOcjeneDto.getOcjena());
			studentService.spremi(student);
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
		List<Prisustvo> prisustva = prisustvoService.dohvatiSve();
		if(prisustva.size() >= 10) {
			uneseneSveVjezbe = true;
		}
		
		List<Student> uspjesniStudenti = new ArrayList<>();
		List<Student> neuspjesniStudenti = new ArrayList<>();
		
		try {
			List<Student> studenti = studentService.dohvatiSve();
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
		LOGGER.debug("Zahtjev za izvoz uspješnih studenata");
		
        List<Student> studentiZaIzvoz = new ArrayList<Student>(); 
        
        try {
			List<Student> studenti = studentService.dohvatiSve();
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
		LOGGER.debug("Zahtjev za izvoz neuspješnih studenata");
		
        List<Student> studentiZaIzvoz = new ArrayList<Student>(); 
        
        try {
			List<Student> studenti = studentService.dohvatiSve();
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
	
	@GetMapping("/obrisiSve")
	public String obrisiSve() {
		LOGGER.debug("Zahtjev za brisanje svih studenata");
		
		prisustvoService.obrisiSve();
		
		studentService.obrisiSve();
		
		return "redirect:/studenti/lista";
	}
	
	@GetMapping("/prikaziStatistiku")
	public String prikaziStatistiku(Model model) {
		LOGGER.debug("Zahtjev za prikaz statistike");
		
		boolean uneseneSveVjezbe = false;
		List<Prisustvo> prisustva = prisustvoService.dohvatiSve();
		if(prisustva.size() >= 10) {
			uneseneSveVjezbe = true;
		}
		
		model.addAttribute("uneseneSveVjezbe", uneseneSveVjezbe);
		
		// vraćamo samo stranicu jer kada se stranica učita poslati će AJAX request na endpoint za dohvat statističkih podataka
		return "studenti/studenti-statistika";
	}
	
	@GetMapping("/dohvatiStatistikuPrisustva")
	public ResponseEntity<Map<Integer, Integer>> dohvatiStatistikuPrisustva() {
		LOGGER.debug("Zahtjev za dohvat statističkih podataka o prisustvu");
		
        Map<Integer, Integer> podaciZaGraf = new TreeMap<>();
		List<Prisustvo> prisustva = prisustvoService.dohvatiSve();
		
		// Dohvaćamo sva prisustva te za svaku vježbu u mapu za graf dodajemo broj prisutnih studenata
		for(int i = 0; i < prisustva.size(); i++) {
			if(!podaciZaGraf.containsKey(prisustva.get(i).getBrojVjezbe())) {
		        podaciZaGraf.put(prisustva.get(i).getBrojVjezbe(), prisustva.get(i).getStudenti().size());
			}
		}
       
        return new ResponseEntity<>(podaciZaGraf, HttpStatus.OK);
    }
	
	@GetMapping("/dohvatiStatistikuOcjena")
	public ResponseEntity<Map<String, Integer>> dohvatiStatistikuOcjena() {
		LOGGER.debug("Zahtjev za dohvat statističkih podataka o ocjenama");
		
		List<Student> studenti = studentService.dohvatiSve();
		Map<String,Integer> podaciZaGraf = new HashMap<>();
		
		// Iteriramo kroz listu svih studenata i punimo mapu za graf sa ocjenama ako postoje
		for(int i = 0; i < studenti.size(); i++) {
			if(studenti.get(i).getOcjena() != 0) {
				if(studenti.get(i).getOcjena() == 1) {
					if(podaciZaGraf.containsKey("Nedovoljan")) {
						podaciZaGraf.put("Nedovoljan", podaciZaGraf.get("Nedovoljan") + 1);
					} else {
						podaciZaGraf.put("Nedovoljan", 1);
					}
				} else if(studenti.get(i).getOcjena() == 2) {
					if(podaciZaGraf.containsKey("Dovoljan")) {
						podaciZaGraf.put("Dovoljan", podaciZaGraf.get("Dovoljan") + 1);
					} else {
						podaciZaGraf.put("Dovoljan", 1);
					}
				} else if(studenti.get(i).getOcjena() == 3) {
					if(podaciZaGraf.containsKey("Dobar")) {
						podaciZaGraf.put("Dobar", podaciZaGraf.get("Dobar") + 1);
					} else {
						podaciZaGraf.put("Dobar", 1);
					}
				} else if(studenti.get(i).getOcjena() == 4) {
					if(podaciZaGraf.containsKey("Vrlo dobar")) {
						podaciZaGraf.put("Vrlo dobar", podaciZaGraf.get("Vrlo dobar") + 1);
					} else {
						podaciZaGraf.put("Vrlo dobar", 1);
					}
				} else if(studenti.get(i).getOcjena() == 5) {
					if(podaciZaGraf.containsKey("Odličan")) {
						podaciZaGraf.put("Odličan", podaciZaGraf.get("Odličan") + 1);
					} else {
						podaciZaGraf.put("Odličan", 1);
					}
				}
			}
		}
       
        return new ResponseEntity<>(podaciZaGraf, HttpStatus.OK);
    }
	
	private List<Student> dohvatiStudenteZaPotpis() {
		List<Prisustvo> prisustva = prisustvoService.dohvatiSve();
		HashMap<Integer,List<Student>> studenti = new HashMap<>();
		HashMap<Student,Integer> brojDolazaka = new HashMap<>();
		List<Student> studentiZaUpis = new ArrayList<>();
		
		List<Student> sviStudenti = studentService.dohvatiSve();
		
		/**
			Iteriramo kroz postojeća prisustva i punimo hashmapu studenti sa brojem vježbe kao ključ.
		**/
		if(prisustva.size() >= 10) {
			for(int i=0; i<prisustva.size(); i++) {
				studenti.put(i,prisustva.get(i).getStudenti());
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
				provjerava se je li broj dolazaka tog studenta veći ili jednak 6 od 10 dolazaka. 
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
		return studentiZaUpis;
	}
}
