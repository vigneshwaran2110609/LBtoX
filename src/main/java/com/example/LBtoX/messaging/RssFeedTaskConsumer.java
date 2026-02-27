package com.example.LBtoX.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.example.LBtoX.models.LetterboxdProfile;
import com.example.LBtoX.models.LetterboxdRssEntry;
import com.example.LBtoX.models.LetterboxdRssFeed;
import com.example.LBtoX.services.RssFeedMessageService;
import com.example.LBtoX.services.RSSFeedService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.ZonedDateTime;

@Component
public class RssFeedTaskConsumer {
	
	@Autowired
	private RSSFeedService rssFeedService;
	
	@Autowired
	private RssFeedMessageService rssFeedMessageService;
	
	@JmsListener(destination = "FeedProcessingQueue")
    public void receiveMessage(Long cycle) {

        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                while (true) {
                    Map<LetterboxdProfile, LetterboxdRssFeed> mainFeedMap = rssFeedMessageService.processBatch(cycle, 1000);
                    for (Map.Entry<LetterboxdProfile, LetterboxdRssFeed> entry
                            : mainFeedMap.entrySet()) {
                    	// System.out.println(entry.getValue());
                    	LetterboxdProfile profile = entry.getKey();
            		    LetterboxdRssFeed feed = entry.getValue();
            		    ZonedDateTime firstEntryPubDate = rssFeedService.getFirstEntryPubDate(feed);
            		    ZonedDateTime pubDateFromDB = profile.getPubDate();
            		    if (firstEntryPubDate != pubDateFromDB) {
            		    	if (feed.getItemCount() != 0) {
            		    		//process all the entries present
            		    		List<LetterboxdRssEntry> feedEntries = feed.getItems();
            		    		for (LetterboxdRssEntry ent: feedEntries) {
            		    			// System.out.println(ent);
            		    			//tweet function		
            		    		}
            		    		//update pubdate function
            		    	}
            		    }
            		    else {
        		    		//process till pubDate is reached or end of list of reached
        		    		List<LetterboxdRssEntry> feedEntries = feed.getItems();
        		    		for (LetterboxdRssEntry ent: feedEntries) {
        		    			if (ent.getPubDate() == pubDateFromDB) {
        		    				break;
        		    			}
        		    			//tweet function
        		    		}
        		    		//update pubdate in letterboxdprofile db function
        		    	}
                    }
            		    
                }
            });
        }

        executor.shutdown();
    }
}