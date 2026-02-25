package com.example.LBtoX.scheduler;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.LBtoX.services.*;
import com.example.LBtoX.repositories.*;
import com.example.LBtoX.messaging.RssFeedTaskProducer;

@Component
public class RSSFeedPollingScheduler {
	
	@Autowired
	private RssFeedTaskProducer producer;
	
	@Autowired
	private LastProcessedCycleService lastProcessedCycleService;
	
	@Autowired
	private CycleService cycleService;
	
	@Autowired
	private LastProcessedIDRepository lastProcessedIDRepository;
	
	@Scheduled(fixedRate = 1000)
	public void processFeeds() {
		Long cycle = lastProcessedCycleService.getLastProcessedCycle();
		cycleService.cleanupCycle(cycle);
		cycle = cycle + 1l;
		lastProcessedIDRepository.updateLastProcessedId(cycle);
		cycleService.startNewCycle(cycle);
		producer.processFeed("FeedProcessingQueue", cycle);
	}

}
