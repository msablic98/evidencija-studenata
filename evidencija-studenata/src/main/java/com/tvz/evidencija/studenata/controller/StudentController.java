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

@Controller
@RequestMapping("/studenti")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private PrisutstvoService prisutstvoService;
	
	@GetMapping("/lista")
	public String getLista(Model model) {
		
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
		
		studentService.save(student);
		
		return "redirect:/studenti/lista";
	}
	
	@GetMapping("/formaZaAzuriranje")
	public String formaZaAzuriranje(@RequestParam("studentId") int id, Model model) {
		
		Student student = studentService.getStudentById(id);
		
		model.addAttribute("student", student);
		
		return "studenti/studenti-forma";
	}
	
	@GetMapping("/obrisiStudenta")
	public String obrisiStudenta(@RequestParam("studentId") int id) {
		
		studentService.deleteById(id);
		
		return "redirect:/studenti/lista";
	}
	
	@GetMapping("/upisiPristustvo")
	public String upisiPrisutstvo(Model model) {
		
		List<Student> studenti = studentService.findAll();
		PrisutstvoDto prisutstvoDto = new PrisutstvoDto();
		
		
		model.addAttribute("studenti", studenti);
		model.addAttribute("prisutstvoDto",prisutstvoDto);
		
		return "studenti/studenti-prisutstvo";
		
	}
	
	@PostMapping("/spremiPrisutstvo")
	public String spremiPrisutstvo(@ModelAttribute("prisutstvoDto") PrisutstvoDto prisutstvoDto) {
		
		Prisutstvo prisutstvo;
		
		Prisutstvo postojecePrisutstvo = prisutstvoService.getPrisutstvoByBrojVjezbe(prisutstvoDto.getBrojVjezbe());
		
		if(postojecePrisutstvo != null) {
			postojecePrisutstvo.getStudenti().add(studentService.getStudentById(prisutstvoDto.getStudentId()));
			
			prisutstvo = postojecePrisutstvo;
		} else {
			prisutstvo = new Prisutstvo();
			
			prisutstvo.setBrojVjezbe(prisutstvoDto.getBrojVjezbe());
			prisutstvo.setStudenti(List.of(studentService.getStudentById(prisutstvoDto.getStudentId())));
		}
		
		prisutstvoService.save(prisutstvo);
		
		System.out.println(prisutstvoService.findAll());
		
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
		
		if(prisutstva.size() == 3) {
			for(int i=0; i<prisutstva.size(); i++) {
				studenti.put(i,prisutstva.get(i).getStudenti());
			}
			
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
			for(int i = 0; i < studenti.size(); i++) {
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
