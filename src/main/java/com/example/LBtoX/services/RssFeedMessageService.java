package com.example.LBtoX.services;

import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.LBtoX.models.*;
import com.example.LBtoX.repositories.*;
import org.springframework.stereotype.Service;

@Service
public class RssFeedMessageService {
	
	@Autowired
	private ProfileProcessingRepository processingRepository;
	
	@Autowired
	private RssFeedRepository profileRepository;
	
	@Autowired
	private RSSFeedService rssFeedService;
	
	@Transactional	
	public Map<LetterboxdProfile, LetterboxdRssFeed> processBatch(Long cycleId, int batchSize) {
		List<Long> ids = processingRepository.lockNextBatch(cycleId, batchSize);

        List<LetterboxdProfile> profiles = profileRepository.findAllById(ids);

        Map<LetterboxdProfile, LetterboxdRssFeed> result = rssFeedService.getFeedsFromProfiles(profiles);

        processingRepository.markDone(cycleId, ids);
        
        return result;
	    
	}

}
