package com.example.LBtoX.models;

import jakarta.persistence.*;

@Entity
@Table(name = "processing_checkpoint")
public class ProcessingCheckpoint {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "last_processed_id")
    private Long lastProcessedId;

    // Constructors
    public ProcessingCheckpoint() {}

    public ProcessingCheckpoint(Long lastProcessedId) {
        this.lastProcessedId = lastProcessedId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLastProcessedId() {
        return lastProcessedId;
    }

    public void setLastProcessedId(Long lastProcessedId) {
        this.lastProcessedId = lastProcessedId;
    }
}
