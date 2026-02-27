package com.example.LBtoX.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.*;
import com.example.LBtoX.models.LetterboxdProfile;
import com.example.LBtoX.repositories.LetterboxdProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class LetterboxdProfileService {
    @Autowired
    private LetterboxdProfileRepository letterboxdProfileRepository;

    public ResponseEntity<Map<String, Object>> saveLetterboxdProfile(LetterboxdProfile letterboxdProfile){
        String letterboxdId = letterboxdProfile.getLetterboxdId();
        if (letterboxdProfileRepository.existsByLetterboxdId(letterboxdId)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Letterboxd profile already exists"));
        }
        ZonedDateTime now = ZonedDateTime.now();
        letterboxdProfile.setPubDate(now);
        letterboxdProfileRepository.save(letterboxdProfile);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Letterboxd profile saved successfully");
        response.put("id", letterboxdProfile.getId());
        response.put("letterboxdId", letterboxdProfile.getLetterboxdId());
        response.put("status", "SAVED");

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }
}