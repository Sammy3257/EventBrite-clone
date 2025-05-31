package com.EventBrite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "ticket_options")
public class TicketOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum TicketType {
        VIP, REGULAR, POPULAR, SINGLE
    }

    @Enumerated(EnumType.STRING)
    @NotNull
    private TicketType type;

    @NotNull
    @Min(value = 0, message = "Price must be 0 or higher")
    private Double price;

    // Many ticket types per one event
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventDisplay event;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull TicketType getType() {
        return type;
    }

    public void setType(@NotNull TicketType type) {
        this.type = type;
    }

    public @NotNull @Min(value = 0, message = "Price must be 0 or higher") Double getPrice() {
        return price;
    }

    public void setPrice(@NotNull @Min(value = 0, message = "Price must be 0 or higher") Double price) {
        this.price = price;
    }

    public EventDisplay getEvent() {
        return event;
    }

    public void setEvent(EventDisplay event) {
        this.event = event;
    }
}
