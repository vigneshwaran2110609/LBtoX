package com.example.LBtoX.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.example.LBtoX.DTO.LetterboxdProfilesIDs;
import com.example.LBtoX.models.LetterboxdProfile;
import com.example.LBtoX.models.LetterboxdRssEntry;
import com.example.LBtoX.models.LetterboxdRssFeed;
import com.example.LBtoX.repositories.ProfileProcessingRepository;
import com.example.LBtoX.services.*;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.ZonedDateTime;
//Change this structure, after i retrieve the rows, put them in a global list (as in outside thread)
//then process each and every entry one by one from that list
// then mark as done.
// this is done because we want to sepearate the processing of the profiles from fetching them
// so even if multiple threads fetch them ( how is this happening or is this happening ? )
// or is this happening because of multiple receivemessage calls?
// if we process them outside of fetching there wont be a chance of muliple times processing of the same record with same value
@Component
public class RssFeedTaskConsumer {
	
	@Autowired
	private RSSFeedService rssFeedService;
	
	@Autowired
	private RssFeedMessageService rssFeedMessageService;

	@Autowired
	private LetterboxdProfileService letterboxdProfileService;
		
	@Autowired
	private ProfileProcessingRepository processingRepository;
	
	@JmsListener(destination = "FeedProcessingQueue", concurrency = "5")
    public void receiveMessage(Long cycle) {
		LetterboxdProfilesIDs temProfilesIDs = rssFeedMessageService.processBatch(cycle, 1000);
		List<LetterboxdProfile> profiles = temProfilesIDs.getProfiles();
		Map<LetterboxdProfile, LetterboxdRssFeed> mainFeedMap = rssFeedService.getFeedsFromProfiles(profiles);
        for (Map.Entry<LetterboxdProfile, LetterboxdRssFeed> entry : mainFeedMap.entrySet()) {
            LetterboxdProfile profile = entry.getKey();
            LetterboxdRssFeed feed = entry.getValue();
            ZonedDateTime firstEntryPubDate = rssFeedService.getFirstEntryPubDate(feed);
        	ZonedDateTime pubDateFromDB = profile.getPubDate();
            if (!firstEntryPubDate.equals(pubDateFromDB)) {
            	if (feed.getItemCount() != 0) {
            		List<LetterboxdRssEntry> feedEntries = feed.getItems();
					letterboxdProfileService.processProfile(profile.getId(), feedEntries);
            	}
			processingRepository.markDone(cycle, temProfilesIDs.getIDs());
    		}
		}
	}
}