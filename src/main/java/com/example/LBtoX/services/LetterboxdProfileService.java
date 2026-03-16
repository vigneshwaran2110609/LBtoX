package com.example.LBtoX.services;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.ZonedDateTime;
import java.util.*;
import com.example.LBtoX.models.LetterboxdProfile;
import com.example.LBtoX.models.LetterboxdRssEntry;
import com.example.LBtoX.repositories.LetterboxdProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LetterboxdProfileService {
    @Autowired
    private LetterboxdProfileRepository letterboxdProfileRepository;

    @Autowired
    private TwitterCredentialService twitterCredentialService;

    @Autowired
	private LetterboxdRssEntryService letterboxdRssEntryService;

    public Boolean saveLetterboxdProfile(LetterboxdProfile letterboxdProfile){
        String letterboxdId = letterboxdProfile.getLetterboxdId();
        if (letterboxdProfileRepository.existsByLetterboxdId(letterboxdId)) {
            return false;
        }
        ZonedDateTime now = ZonedDateTime.now();
        letterboxdProfile.setPubDate(now);
        letterboxdProfileRepository.save(letterboxdProfile);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Letterboxd profile saved successfully");
        response.put("id", letterboxdProfile.getId());
        response.put("letterboxdId", letterboxdProfile.getLetterboxdId());
        response.put("status", "SAVED");
        return true;
    }

    @Transactional
    public void processProfile(Long profileId, List<LetterboxdRssEntry> feedEntries) {
        LetterboxdProfile profile = letterboxdProfileRepository.findByIdForUpdate(profileId);
        ZonedDateTime pubDateFromDB = profile.getPubDate();
        for (LetterboxdRssEntry ent : feedEntries) {
            if (ent.getPubDate().isAfter(pubDateFromDB)) {
                String review = letterboxdRssEntryService.extractLastParagraphText(ent.getDescription());
				String movie = ent.getFilmTitle();
				Double rating = letterboxdRssEntryService.parseRating(ent.getDisplayRating());
				String post = movie + "\n" + rating + "\n" + review;
                try {
					twitterCredentialService.postTweet(profile.getLetterboxdId(),post);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        }
        if (!feedEntries.isEmpty()) {
            ZonedDateTime latest = feedEntries.get(0).getPubDate();
            profile.setPubDate(latest);
        }
        letterboxdProfileRepository.save(profile);
    }
}