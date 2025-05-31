package com.EventBrite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;

    private String fullName;
    private String phone;
    private String ticketType;
    private String reference;
    private Double price;

    @ManyToOne
    private EventDisplay event;
}

