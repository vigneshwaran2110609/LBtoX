package com.example.LBtoX.models;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "twitter_credentials")
public class TwitterCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "letterboxd_id", nullable = false, unique = true)
    private String letterboxdId;

    @Column(name = "twitter_id", nullable = true)
    private String twitterId;

    @Column(name = "twitter_handle", nullable = true)
    private String twitterHandle;

    @Column(name = "access_token", nullable = true, columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "access_token_secret", nullable = true, columnDefinition = "TEXT")
    private String accessTokenSecret;

    @Column(name = "bearer_token", columnDefinition = "TEXT")
    private String bearerToken;

    @Column(name = "oauth_state")
    private String oauthState;

    @Column(name = "code_verifier")
    private String codeVerifier;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public TwitterCredential() {}

    public TwitterCredential(String letterboxdId, String twitterId, String twitterHandle,
                           String accessToken, String accessTokenSecret) {
        this.letterboxdId = letterboxdId;
        this.twitterId = twitterId;
        this.twitterHandle = twitterHandle;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
        this.createdAt = ZonedDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLetterboxdId() {
        return letterboxdId;
    }

    public void setLetterboxdId(String letterboxdId) {
        this.letterboxdId = letterboxdId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public String getOauthState() {
        return oauthState;
    }

    public void setOauthState(String oauthState) {
        this.oauthState = oauthState;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }

    public void setCodeVerifier(String codeVerifier) {
        this.codeVerifier = codeVerifier;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
