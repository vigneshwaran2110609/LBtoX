package com.example.LBtoX.scheduler;

import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import com.example.LBtoX.services.*;
import com.example.LBtoX.models.*;
import java.util.*;

@Component
public class RSSFeedPollingScheduler {
	
	private final RSSFeedService rssFeedService;
	
	public RSSFeedPollingScheduler(RSSFeedService rssFeedService) {
		this.rssFeedService = rssFeedService;
	}
	
	@Scheduled(fixedRate = 5000)
	public void processFeeds() {
		LetterboxdRssFeed letterboxdRssFeed = this.rssFeedService.getRssFeed("vignesh27082003");
		List<LetterboxdRssEntry> entries = letterboxdRssFeed.getItems();
		for (LetterboxdRssEntry entry : entries) {
			System.out.println(entry.getFilmTitle());
	    }
	}

}
