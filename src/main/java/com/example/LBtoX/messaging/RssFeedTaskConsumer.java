package com.example.LBtoX.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.example.LBtoX.models.LetterboxdProfile;
import com.example.LBtoX.models.LetterboxdRssEntry;
import com.example.LBtoX.models.LetterboxdRssFeed;
import com.example.LBtoX.repositories.LetterboxdProfileRepository;
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
	
	@JmsListener(destination = "FeedProcessingQueue")
    public void receiveMessage(Long cycle) {

        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                while (true) {
                    Map<LetterboxdProfile, LetterboxdRssFeed> mainFeedMap = rssFeedMessageService.processBatch(cycle, 1000);
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
										System.out.print(post);
										twitterCredentialService.postTweet(lbId,post);
									}
            		    		}
            		    		//update pubdate function
								LetterboxdRssEntry lastEntry = feedEntries.get(0);
								ZonedDateTime latestPubDateOfProfile = lastEntry.getPubDate();
								profile.setPubDate(latestPubDateOfProfile);
								System.out.println(profile.getPubDate());
								letterboxdProfileRepository.save(profile);
            		    	}
            		    }
                    }
            		    
                }
            });
        }
        executor.shutdown();
    }
}