package com.example.LBtoX.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.example.LBtoX.services.RssFeedMessageService;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class RssFeedTaskConsumer {
	
	@Autowired
	private RssFeedMessageService rssFeedMessageService;

    @JmsListener(destination = "FeedProcessingQueue") 
    public void receiveMessage(String message) {
    	if (message.equals("ProcessRssFeeds")) {
    		rssFeedMessageService.processRssFeed();
    	}
    }
 
}
