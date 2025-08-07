package com.example.LBtoX.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@JacksonXmlRootElement(localName = "item")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LetterboxdRssEntry {

    private String title;
    private String link;

    @JacksonXmlProperty(localName = "guid")
    private String guid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE, dd MMM yyyy HH:mm:ss Z")
    @JacksonXmlProperty(localName = "pubDate")
    private ZonedDateTime pubDate;

    @JacksonXmlProperty(namespace = "https://letterboxd.com", localName = "watchedDate")
    private LocalDate watchedDate;

    @JacksonXmlProperty(namespace = "https://letterboxd.com", localName = "rewatch")
    private String rewatch;

    @JacksonXmlProperty(namespace = "https://letterboxd.com", localName = "filmTitle")
    private String filmTitle;

    @JacksonXmlProperty(namespace = "https://letterboxd.com", localName = "filmYear")
    private Integer filmYear;

    @JacksonXmlProperty(namespace = "https://letterboxd.com", localName = "memberRating")
    private Double memberRating;

    @JacksonXmlProperty(namespace = "https://themoviedb.org", localName = "movieId")
    private Long tmdbMovieId;

    private String description;

    @JacksonXmlProperty(namespace = "http://purl.org/dc/elements/1.1/", localName = "creator")
    private String creator;

    // Default constructor
    public LetterboxdRssEntry() {}

    // Constructor with essential fields
    public LetterboxdRssEntry(String title, String link, String guid, ZonedDateTime pubDate, 
                             String filmTitle, Integer filmYear) {
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.pubDate = pubDate;
        this.filmTitle = filmTitle;
        this.filmYear = filmYear;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public ZonedDateTime getPubDate() {
        return pubDate;
    }

    public void setPubDate(ZonedDateTime pubDate) {
        this.pubDate = pubDate;
    }

    public LocalDate getWatchedDate() {
        return watchedDate;
    }

    public void setWatchedDate(LocalDate watchedDate) {
        this.watchedDate = watchedDate;
    }

    public String getRewatch() {
        return rewatch;
    }

    public void setRewatch(String rewatch) {
        this.rewatch = rewatch;
    }

    public boolean isRewatch() {
        return "Yes".equalsIgnoreCase(rewatch);
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }

    public Integer getFilmYear() {
        return filmYear;
    }

    public void setFilmYear(Integer filmYear) {
        this.filmYear = filmYear;
    }

    public Double getMemberRating() {
        return memberRating;
    }

    public void setMemberRating(Double memberRating) {
        this.memberRating = memberRating;
    }

    public Long getTmdbMovieId() {
        return tmdbMovieId;
    }

    public void setTmdbMovieId(Long tmdbMovieId) {
        this.tmdbMovieId = tmdbMovieId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    // Utility methods
    public String getDisplayRating() {
        if (memberRating == null) {
            return "No rating";
        }
        
        int fullStars = memberRating.intValue();
        boolean hasHalfStar = memberRating % 1 != 0;
        
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < fullStars; i++) {
            stars.append("★");
        }
        if (hasHalfStar) {
            stars.append("½");
        }
        
        return stars.toString();
    }

    public String extractImageUrl() {
        if (description == null) return null;
        
        // Extract image URL from CDATA description
        int imgStart = description.indexOf("<img src=\"");
        if (imgStart == -1) return null;
        
        imgStart += 10; // length of "<img src=\""
        int imgEnd = description.indexOf("\"", imgStart);
        if (imgEnd == -1) return null;
        
        return description.substring(imgStart, imgEnd);
    }

    @Override
    public String toString() {
        return "LetterboxdRssEntry{" +
                "title='" + title + '\'' +
                ", filmTitle='" + filmTitle + '\'' +
                ", filmYear=" + filmYear +
                ", memberRating=" + memberRating +
                ", watchedDate=" + watchedDate +
                ", isRewatch=" + isRewatch() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LetterboxdRssEntry that = (LetterboxdRssEntry) o;

        return guid != null ? guid.equals(that.guid) : that.guid == null;
    }

    @Override
    public int hashCode() {
        return guid != null ? guid.hashCode() : 0;
    }
}