package com.example.LBtoX.services;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.LBtoX.models.LetterboxdProfile;
import com.example.LBtoX.models.LetterboxdRssFeed;
import com.example.LBtoX.repositories.RssFeedRepository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RssFeedMessageService {
	
	@Autowired
	private ProcessingCheckpointService processingCheckpointService;
	
	@Autowired
	private RssFeedRepository rssFeedRepository;
	
	@Autowired
	private RSSFeedService rssFeedService;
	
	@Transactional	
	public Map<LetterboxdProfile, LetterboxdRssFeed> processRssFeed() {
	    ExecutorService executor = Executors.newFixedThreadPool(5);
	    Map<LetterboxdProfile, LetterboxdRssFeed> mainFeedMap = new ConcurrentHashMap<>();
	    
	    for (int i = 1; i <= 5; i++) {
	        executor.submit(() -> {
	            Long startId = processingCheckpointService.getLastProcessedId();
	            Long endId = startId + 200l;
	            List<LetterboxdProfile> profiles = rssFeedRepository.findByIdBetween(startId, endId);
	            Map<LetterboxdProfile, LetterboxdRssFeed> feedMap = rssFeedService.getFeedsFromProfiles(profiles);
	            mainFeedMap.putAll(feedMap);
	        });
	    }
	    
	    executor.shutdown();
	    
	    // have to understand this part of the code completely
	    try {
	        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt();
	    }
	    
	    //my logic here , or call a function
	    return mainFeedMap;
	    
	}

}
