package com.EventBrite.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class attendeeHome {

    @GetMapping("/Attendee-Home")
    public String attendeeHomeShow(){
        return"Attendee-Home";
    }

    @GetMapping("/Venue-Explore")
    public String venueExplore(){
        return"Venue-Explore";
    }

    @GetMapping("/Attendee-Tickets")
    public String tickets(){
        return"Attendee-Tickets";
    }


    @GetMapping("/bookVenue")
    public String bookVenue(Model model){
        model.addAttribute("message", "Booking Venue not available now");
        return"bookVenue";
    }
}
