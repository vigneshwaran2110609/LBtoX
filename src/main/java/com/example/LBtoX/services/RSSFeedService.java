package com.example.LBtoX.services;

import java.util.*;
import com.example.LBtoX.models.LetterboxdProfile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.LBtoX.models.LetterboxdRssFeed;

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
	
	public Map<LetterboxdProfile, LetterboxdRssFeed> getFeedsFromProfiles(List<LetterboxdProfile> profiles) {
	    Map<LetterboxdProfile, LetterboxdRssFeed> feedMap = new HashMap<>();
	    for (LetterboxdProfile profile : profiles) {
	        LetterboxdRssFeed feed = getRssFeed(profile.getLetterboxdId());
	        feedMap.put(profile, feed);
	    }
	    return feedMap;
	}

}
