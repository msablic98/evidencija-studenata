package com.tvz.evidencija.studenata.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tvz.evidencija.studenata.dao.StudentRepository;
import com.tvz.evidencija.studenata.entity.Student;

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
 * Ova klasa sadrži implementaciju svih metoda za manipuliranje Student podacima.
 * 
 */

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;
	
	@Override
	public List<Student> findAll() {

		return studentRepository.findAll();
	}

	@Override
	public Student getStudentById(int id) {

		Optional<Student> student =  studentRepository.findById(id);
	
		Student studentResult = null;
		
		if(student.isPresent()) {
			studentResult = student.get();
		} else {
			throw new RuntimeException("Student ne postoji sa ID oznakom: " + id);
		}
		return studentResult;
	}

	@Override
	public void save(Student student) {

		studentRepository.save(student);
	}

	@Override
	public void deleteById(int id) {

		studentRepository.deleteById(id);
	}

}
