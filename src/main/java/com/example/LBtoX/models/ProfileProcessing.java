package com.example.LBtoX.models;
import jakarta.persistence.*;
@Entity
@Table(name = "profile_processing")
public class ProfileProcessing {

    @EmbeddedId
    private ProfileProcessingId id;

    private String status; // PENDING, PROCESSING, DONE

    public ProfileProcessing() {
    }

    public ProfileProcessingId getId() {
        return id;
    }

    public void setId(ProfileProcessingId id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}