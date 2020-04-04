package com.tvz.evidencija.studenata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tvz.evidencija.studenata.dao.PrisutstvoRepository;
import com.tvz.evidencija.studenata.entity.Prisutstvo;

@Service
public class PrisutstvoServiceImpl implements PrisutstvoService {

	@Autowired
	private PrisutstvoRepository prisutstvoRepository;
	
	@Override
	public List<Prisutstvo> findAll() {

		return prisutstvoRepository.findAll();
	}

	@Override
	public void save(Prisutstvo prisutstvo) {

		prisutstvoRepository.save(prisutstvo);
	}

}
