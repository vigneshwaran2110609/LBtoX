package com.example.LBtoX.models;

import jakarta.persistence.*;

@Entity
@Table(name = "letterboxd_profiles")
public class LetterboxdProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // matches PostgreSQL IDENTITY
    private Long id;

    @Column(name = "letterboxd_id", nullable = false, unique = true)
    private String letterboxdId;

    // --- Constructors ---
    public LetterboxdProfile() {}

    public LetterboxdProfile(String letterboxdId) {
        this.letterboxdId = letterboxdId;
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
