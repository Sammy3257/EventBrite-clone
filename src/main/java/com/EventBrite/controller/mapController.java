package com.EventBrite.controller;

import com.EventBrite.model.Venue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class mapController {

    @GetMapping("/eventArea")
    public String eventArea(){
        return"EventArea";
    }

    @PostMapping("/eventArea")
    public String handlePost() {
        // process form data or payload here
        return "EventArea"; // or redirect or return JSON
    }

    @GetMapping("/venues/{id}")
    public String venueDetails(@PathVariable("id") int id, Model model) {
        List<Venue> venues = List.of(
                new Venue(1, "Accra International Conference Centre", "Accra", "A prime location in Accra for major conferences, concerts and exhibitions.", 5.556, -0.1969, 5, "/images/venue1.jpeg"),
                new Venue(2, "University of Ghana, Legon", "Accra", "A open space in Accra for major conferences, concerts and exhibitions.", 5.655, -0.186, 5, "/images/venue2.jpg"),
                new Venue(3, "Golden Tulip Hotel Accra", "Accra", "A prime location in Accra for major conferences, concerts and exhibitions.", 5.577, -0.184, 5, "/images/venue3.jpg"),
                new Venue(4, "Labadi Beach Hotel", "Accra", "A seaside luxury venue for top-tier events and entertainment.", 5.561, -0.169, 5, "/images/venue4.jpg"),
                new Venue(5, "Ghana International Trade Fair Center", "Accra", "Host of large exhibitions, fairs, and expos.", 5.578, -0.118, 5, "/images/venue5.jpeg"),
                new Venue(6, "Kempinski Hotel Gold Coast City", "Accra", "Luxury venue in Accra hosting corporate and entertainment events.", 5.558, -0.2005, 4, "/images/venue6.jpeg"),
                new Venue(7, "Baba Yara Sports Stadium, Kumasi", "Kumasi", "Great for sports, rallies, and large concerts in the Ashanti Region.", 6.6884, -1.6244, 3, "/images/venue7.webp"),
                new Venue(8, "Kumasi Culture Centre", "Kumasi", "Cultural hub in Kumasi for exhibitions, music, and traditional events.", 6.6884, -1.621, 3, "/images/venue8.jpg"),
                new Venue(9, "Takoradi Mall", "Takoradi", "A regional venue for promotional events, markets, and shows.", 4.8998, -1.7752, 3, "/images/venue9.jpg"),
                new Venue(10, "Best Western Plus Atlantic Hotel", "Takoradi", "Seaside location for weddings, conferences, and social events.", 4.902, -1.767, 4, "/images/venue10.jpg"),
                new Venue(11, "RayBow Hotel", "Takoradi", "Ideal for social gatherings and meetings by the beach.", 4.903, -1.766, 4, "/images/venue11.jpeg"),
                new Venue(12, "Villa Beach Hotel", "Takoradi", "Coastal resort venue with event space and entertainment.", 4.904, -1.765, 4, "/images/venue12.jpeg"),
                new Venue(13, "Akroma Plaza", "Takoradi", "Multi-purpose event center in the Western Region.", 4.905, -1.764, 4, "/images/venue13.jpeg"),
                new Venue(13, "Lasenza", "Takoradi", "Lasenza", 4.705, -1.754, 4, "/images/venue14.jpeg")
        );

        Venue venue = venues.stream().filter(v -> v.getId() == id).findFirst().orElse(null);
        if (venue != null) {
            model.addAttribute("venue", venue);
        }

        return "venue-details";
    }






}
