package com.example.LBtoX.scheduler;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.LBtoX.services.*;
import com.example.LBtoX.models.*;
import com.example.LBtoX.messaging.RssFeedTaskProducer;
import java.util.*;

@Component
public class RSSFeedPollingScheduler {
	
	@Autowired
	private RssFeedTaskProducer producer;
	
	@Autowired
	private LastProcessedCycleService lastProcessedCycleService;
	
	@Scheduled(fixedRate = 1000)
	public void processFeeds() {
		Long cycle = lastProcessedCycleService.getLastProcessedCycle();
		producer.processFeed("FeedProcessingQueue", cycle);
	}

}
