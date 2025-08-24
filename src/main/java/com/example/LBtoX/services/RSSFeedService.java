package com.example.LBtoX.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.LBtoX.models.*;

@Service
public class RSSFeedService {
	
	private final RestTemplate restTemplate;
	
	public RSSFeedService() {
		this.restTemplate = new RestTemplate();
	}
	
	public LetterboxdRssFeed getRssFeed(String userName) {
		String rssUrl = "https://letterboxd.com/" + userName + "/rss";
        LetterboxdRssFeed letterboxdRssEntry =  restTemplate.getForObject(rssUrl, LetterboxdRssFeed.class);
        return letterboxdRssEntry;
	}

}
