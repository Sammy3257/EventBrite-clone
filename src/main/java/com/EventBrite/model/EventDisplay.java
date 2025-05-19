package com.EventBrite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "event_display")
public class EventDisplay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Information
    @NotBlank(message = "Title is required")
    private String title;

    public @NotBlank(message = "Title is required") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") String title) {
        this.title = title;
    }

    @Size(max = 480, message = "Summary must be 480 characters")
    private String summary;

    public @Size(max = 480, message = "Summary must be ≤480 characters") String getSummary() {
        return summary;
    }

    public void setSummary(@Size(max = 480, message = "Summary must be ≤140 characters") String summary) {
        this.summary = summary;
    }

    // Media
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    private String videoPath;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    // Date & Time
    @Future(message = "Event date must be in the future")
    private LocalDate eventDate;

    public @Future(message = "Event date must be in the future") LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(@Future(message = "Event date must be in the future") LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    public @NotNull(message = "Start time is required") LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(@NotNull(message = "Start time is required") LocalTime startTime) {
        this.startTime = startTime;
    }

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    public @NotNull(message = "End time is required") LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(@NotNull(message = "End time is required") LocalTime endTime) {
        this.endTime = endTime;
    }

    // Location
    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    private String locationDetails;

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    // Event Type
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    // Rich Text Content
    @Lob
    @Column(length = 10000)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Timestamps
    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private LocalDateTime updatedAt;

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum LocationType {
        VENUE, ONLINE, TBA
    }

    public enum EventType {
        SINGLE, RECURRING
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
