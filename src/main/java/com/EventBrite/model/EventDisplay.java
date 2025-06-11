package com.EventBrite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event_display")
public class EventDisplay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Info
    @NotBlank(message = "Title is required")
    private String title;

    @Size(max = 480, message = "Summary must be 480 characters")
    private String summary;

    // Media
    private String imagePath;
    private String videoPath;

    // Date & Time
    @Future(message = "Event date must be in the future")
    private LocalDate eventDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    // Location
    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    private String locationDetails;

    // Event Type
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    // Description
    @Lob
    @Column(length = 10000)
    private String description;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Ticket Options
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketOption> ticketOptions = new ArrayList<>();

    private Boolean isPaid;

    @Enumerated(EnumType.STRING)
    private TicketType paidType;

    private Double priceVip;
    private Double priceRegular;
    private Double pricePopular;
    private Double priceSingle;



    // ENUMS
    public enum LocationType {
        VENUE, ONLINE, TBA
    }

    public enum EventType {
        SINGLE, RECURRING
    }

    public enum TicketType {
        VIP, REGULAR, POPULAR, SINGLE
    }

    // GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<TicketOption> getTicketOptions() {
        return ticketOptions;
    }

    public void setTicketOptions(List<TicketOption> ticketOptions) {
        this.ticketOptions = ticketOptions;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public TicketType getPaidType() {
        return paidType;
    }

    public void setPaidType(TicketType paidType) {
        this.paidType = paidType;
    }

    public Double getPriceVip() {
        return priceVip;
    }

    public void setPriceVip(Double priceVip) {
        this.priceVip = priceVip;
    }

    public Double getPriceRegular() {
        return priceRegular;
    }

    public void setPriceRegular(Double priceRegular) {
        this.priceRegular = priceRegular;
    }

    public Double getPricePopular() {
        return pricePopular;
    }

    public void setPricePopular(Double pricePopular) {
        this.pricePopular = pricePopular;
    }

    public Double getPriceSingle() {
        return priceSingle;
    }

    public void setPriceSingle(Double priceSingle) {
        this.priceSingle = priceSingle;
    }
}
