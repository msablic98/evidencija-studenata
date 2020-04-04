package com.tvz.evidencija.studenata.controller;

import java.util.ArrayList;
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
	
//	@GetMapping("/upisiOcjenu")
//	public String upisiOcjenu(Model model) {
//		
//		List<Prisutstvo> prisutstva = prisutstvoService.findAll();
//		List<Student> studenti = new ArrayList<>();
//		for(int i=0; i<prisutstva.size(); i++) {
//			studenti.add(studentService.getStudentById(prisutstva.get(i).getStudentId()));
//		}
//		
//		model.addAttribute("studenti", studenti);
//		
//		return "studenti/studenti-ocjene";
//	}
	
}
