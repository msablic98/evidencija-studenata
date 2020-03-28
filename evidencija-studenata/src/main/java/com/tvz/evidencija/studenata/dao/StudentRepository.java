package com.tvz.evidencija.studenata.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tvz.evidencija.studenata.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
