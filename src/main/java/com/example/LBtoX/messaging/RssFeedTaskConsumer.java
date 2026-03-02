package com.example.LBtoX.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.example.LBtoX.DTO.LetterboxdProfilesIDs;
import com.example.LBtoX.models.LetterboxdProfile;
import com.example.LBtoX.models.LetterboxdRssEntry;
import com.example.LBtoX.models.LetterboxdRssFeed;
import com.example.LBtoX.repositories.LetterboxdProfileRepository;
import com.example.LBtoX.repositories.ProfileProcessingRepository;
import com.example.LBtoX.services.RssFeedMessageService;
import com.example.LBtoX.services.TwitterCredentialService;
import com.example.LBtoX.services.LetterboxdRssEntryService;
import com.example.LBtoX.services.RSSFeedService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
	private TwitterCredentialService twitterCredentialService;

	@Autowired
	private LetterboxdRssEntryService letterboxdRssEntryService;

	@Autowired
	private LetterboxdProfileRepository letterboxdProfileRepository;
		
	@Autowired
	private ProfileProcessingRepository processingRepository;
	
	@JmsListener(destination = "FeedProcessingQueue")
    public void receiveMessage(Long cycle) {

        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
			
            executor.submit(() -> {
					LetterboxdProfilesIDs temProfilesIDs = rssFeedMessageService.processBatch(cycle, 1000);
					List<LetterboxdProfile> profiles = temProfilesIDs.getProfiles();
					Map<LetterboxdProfile, LetterboxdRssFeed> mainFeedMap = rssFeedService.getFeedsFromProfiles(profiles);
                    for (Map.Entry<LetterboxdProfile, LetterboxdRssFeed> entry : mainFeedMap.entrySet()) {
                    	LetterboxdProfile profile = entry.getKey();
						String lbId = profile.getLetterboxdId();
            		    LetterboxdRssFeed feed = entry.getValue();
            		    ZonedDateTime firstEntryPubDate = rssFeedService.getFirstEntryPubDate(feed);
            		    ZonedDateTime pubDateFromDB = profile.getPubDate();
						//process only if the pubdate stored in db is not same as the pub date from db
						//latest entries come first in the list
						//lets say i watched a film on 3rd march, signed in on 4th march
						//only the movies i watch after 4th march should be tweeted
            		    if (!firstEntryPubDate.equals(pubDateFromDB)) {
            		    	if (feed.getItemCount() != 0) {
            		    		//process all the entries present during the first
            		    		List<LetterboxdRssEntry> feedEntries = feed.getItems();
            		    		for (LetterboxdRssEntry ent: feedEntries) {
									//tweet only if entry date is after pubdatefromdb
									if ( ent.getPubDate().isAfter(pubDateFromDB)){
										// System.out.println("is true" + ent.getPubDate() + pubDateFromDB);
										String review = letterboxdRssEntryService.extractLastParagraphText(ent.getDescription());
										String movie = ent.getFilmTitle();
										Double rating = letterboxdRssEntryService.parseRating(ent.getDisplayRating());
										String post = movie + "\n" + rating + "\n" + review;
										// System.out.print(post);
										try {
											twitterCredentialService.postTweet(lbId,post);
										} catch (Exception e) {
											e.printStackTrace();
										}
										System.out.println("outt");
									}
            		    		}	
            		    		//update pubdate function
								LetterboxdRssEntry lastEntry = feedEntries.get(0);
								ZonedDateTime latestPubDateOfProfile = lastEntry.getPubDate();
								profile.setPubDate(latestPubDateOfProfile);
								System.out.println(profile.getPubDate());
								System.out.println("xx");
								letterboxdProfileRepository.save(profile);
            		    	}
            		    }
                    }
            	processingRepository.markDone(cycle, temProfilesIDs.getIDs());	    
                
            });
		}
        executor.shutdown();
    }
}