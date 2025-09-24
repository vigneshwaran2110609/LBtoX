package com.example.LBtoX.services;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.LBtoX.models.LetterboxdProfile;
import com.example.LBtoX.models.LetterboxdRssFeed;
import com.example.LBtoX.repositories.RssFeedRepository;

@Component
public class RssFeedMessageService {
	
	@Autowired
	private ProcessingCheckpointService processingCheckpointService;
	
	@Autowired
	private RssFeedRepository rssFeedRepository;
	
	@Transactional	
	public void processRssFeed() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 5; i++) {
        	executor.submit(() -> {
                // Action each thread will perform
        		Long startId = processingCheckpointService.getLastProcessedId();
        		Long endId = startId + 200l;
        		List<LetterboxdProfile> profiles = rssFeedRepository.findByIdBetween(startId,endId);
        		List<LetterboxdRssFeed> feeds = 
            });
        }
	}

}
