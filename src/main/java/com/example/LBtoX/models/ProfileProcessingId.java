package com.example.LBtoX.models;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class ProfileProcessingId implements Serializable {

    private Long profileId;
    private Long cycleId;
    
    public ProfileProcessingId() {
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getCycleId() {
        return cycleId;
    }

    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

}
