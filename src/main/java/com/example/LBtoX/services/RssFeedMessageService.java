package com.example.LBtoX.services;

import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.LBtoX.DTO.LetterboxdProfilesIDs;
import com.example.LBtoX.models.*;
import com.example.LBtoX.repositories.*;
import org.springframework.stereotype.Service;

@Service
public class RssFeedMessageService {
	
	@Autowired
	private ProfileProcessingRepository processingRepository;
	
	@Autowired
	private RssFeedRepository profileRepository;
	
	@Transactional
	public LetterboxdProfilesIDs processBatch(Long cycleId, int batchSize) {
		List<Long> ids = processingRepository.lockNextBatch(cycleId, batchSize);
		if (!ids.isEmpty()) {
        	processingRepository.markInProgress(ids);
    	}
        List<LetterboxdProfile> profiles = profileRepository.findAllById(ids);
        return new LetterboxdProfilesIDs(ids,profiles);
	}

}
