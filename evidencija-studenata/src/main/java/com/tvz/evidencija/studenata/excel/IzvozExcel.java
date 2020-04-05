package com.tvz.evidencija.studenata.excel;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.tvz.evidencija.studenata.entity.Student;

/**
 * 
 * @author msablic
 *
 * Klasa služi za generiranje Excel datoteka pomoću podataka koje pošaljemo u modelu studentiZaIzvoz.
 *
 */

public class IzvozExcel extends AbstractXlsView {

	 @Override
	 protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
	  
			 response.setHeader("Content-Disposition", "attachment;filename=\"studenti.xls\"");
			 List<Student> studentList = (List<Student>) model.get("studentiZaIzvoz");
			 Sheet sheet = workbook.createSheet("Podaci o prolaznosti");
			 Row header = sheet.createRow(0);
			 header.createCell(0).setCellValue("Student ID");
			 header.createCell(1).setCellValue("Ime");
			 header.createCell(2).setCellValue("Prezime");
			 header.createCell(3).setCellValue("Email");
			 header.createCell(4).setCellValue("JMBAG");
			 header.createCell(5).setCellValue("Ocjena");
			  
			 int rowNum = 1;
			 for(Student student:studentList){
				 Row row = sheet.createRow(rowNum++);
				 row.createCell(0).setCellValue(student.getId());
				 row.createCell(1).setCellValue(student.getIme());
				 row.createCell(2).setCellValue(student.getPrezime());
				 row.createCell(3).setCellValue(student.getEmail());
				 row.createCell(4).setCellValue(student.getJmbag());
				 if(Integer.valueOf(student.getOcjena()).equals(Integer.valueOf(0))) {
					 row.createCell(5).setCellValue("Nije zadovoljio dolaske");
				 } else {
					 row.createCell(5).setCellValue(student.getOcjena());
				 }
				 
			 }
	 }
	
}
