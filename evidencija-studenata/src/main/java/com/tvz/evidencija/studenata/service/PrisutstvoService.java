package com.tvz.evidencija.studenata.service;

import java.util.List;

import com.tvz.evidencija.studenata.entity.Prisutstvo;

public interface PrisutstvoService {

	public Prisutstvo getPrisutstvoByBrojVjezbe(int brojVjezbe);
	
	public List<Prisutstvo> findAll();
	
	public void save(Prisutstvo prisutstvo);
	
}
