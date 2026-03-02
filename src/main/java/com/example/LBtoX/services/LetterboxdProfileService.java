package com.example.LBtoX.services;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.ZonedDateTime;
import java.util.*;
import com.example.LBtoX.models.LetterboxdProfile;
import com.example.LBtoX.repositories.LetterboxdProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class LetterboxdProfileService {
    @Autowired
    private LetterboxdProfileRepository letterboxdProfileRepository;

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
}