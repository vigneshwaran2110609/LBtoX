package com.example.LBtoX.services;

import com.example.LBtoX.repositories.*;
import com.example.LBtoX.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProcessingCheckpointService {
	
	@Autowired
	public ProcessingCheckpointRepository processingCheckpointRepository;
	
	@Transactional
	public Long getLastProcessedId() {
		ProcessingCheckpoint processingCheckpoint = processingCheckpointRepository.findCheckpoint();
		Long lastProcessedId = processingCheckpoint.getLastProcessedId();
		Long newRowValue = lastProcessedId + 200l;
		int updated = processingCheckpointRepository.updateLastProcessedId(newRowValue);
		return lastProcessedId;
	}
	
}
