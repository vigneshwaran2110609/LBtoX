package com.example.LBtoX.models;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "letterboxd_profiles")
public class LetterboxdProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // matches PostgreSQL IDENTITY
    private Long id;

    @Column(name = "letterboxd_id", nullable = false, unique = true)
    private String letterboxdId;
    
    @Column(name = "latest_pub_date")
    private ZonedDateTime pubDate;

    // --- Constructors ---
    public LetterboxdProfile() {}

    public LetterboxdProfile(String letterboxdId) {
        this.letterboxdId = letterboxdId;
    }
    
    public void setPubDate(ZonedDateTime pubDate) {
    	this.pubDate = pubDate;
    }
    
    public ZonedDateTime getPubDate() {
    	return this.pubDate;
    }

    public Long getId() {
        return id;
    }

    public String getLetterboxdId() {
        return letterboxdId;
    }

    public void setLetterboxdId(String letterboxdId) {
        this.letterboxdId = letterboxdId;
    }
}
