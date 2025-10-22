package com.example.LBtoX.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.example.LBtoX.models.LetterboxdProfile;
import com.example.LBtoX.models.LetterboxdRssFeed;
import com.example.LBtoX.services.RssFeedMessageService;
import com.example.LBtoX.services.RSSFeedService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.ZonedDateTime;

@Component
public class RssFeedTaskConsumer {
	
	@Autowired
	private RssFeedMessageService rssFeedMessageService;
	
	@Autowired
	private RSSFeedService rssFeedService;

    @JmsListener(destination = "FeedProcessingQueue") 
    public void receiveMessage(String message) {
    	if (message.equals("ProcessRssFeeds")) {
    		Map<LetterboxdProfile, LetterboxdRssFeed> mainFeedMap = rssFeedMessageService.processRssFeed();
    		for (Map.Entry<LetterboxdProfile, LetterboxdRssFeed> entry : mainFeedMap.entrySet()) {
    		    LetterboxdProfile profile = entry.getKey();
    		    LetterboxdRssFeed feed = entry.getValue();
    		    ZonedDateTime firstEntryPubDate = rssFeedService.getFirstEntryPubDate(feed);
    		    ZonedDateTime pubDateFromDB = profile.getPubDate();
    		    if (firstEntryPubDate != pubDateFromDB) {
    		    	//process the entries.
    		    }
    		}

    	}
    }
 
}
