package com.tvz.evidencija.studenata.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tvz.evidencija.studenata.dao.StudentRepository;
import com.tvz.evidencija.studenata.entity.Student;

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
