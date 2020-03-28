package com.tvz.evidencija.studenata.service;

import java.util.List;

import com.tvz.evidencija.studenata.entity.Student;

public interface StudentService {

	public List<Student> findAll();
	
	public Student getStudentById(int id);
	
	public void save(Student student);
	
	public void deleteById(int id);
	
}
