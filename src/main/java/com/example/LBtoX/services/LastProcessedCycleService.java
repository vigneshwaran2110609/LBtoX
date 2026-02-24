package com.example.LBtoX.services;

import com.example.LBtoX.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;

public class LastProcessedCycleService {

	@Autowired
	private LastProcessedIDRepository lp;
	
	public Long getLastProcessedCycle() {
		return lp.getCheckpoint().getLastProcessedId();
	}
}
