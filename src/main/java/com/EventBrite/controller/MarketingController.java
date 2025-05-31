package com.EventBrite.controller;

import com.EventBrite.model.Campaign;
import com.EventBrite.model.EventDisplay;
import com.EventBrite.repository.EventDisplayRepository;
import com.EventBrite.repository.UserRepository;
import com.EventBrite.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MarketingController {

    private final EventDisplayRepository eventRepository;

    public MarketingController (EventDisplayRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/marketing")
    public String marketingPage(Model model) {
        EventDisplay event = eventRepository.findById(1L) // or whatever ID makes sense
                .orElseThrow(() -> new RuntimeException("Event not found"));
        List<Campaign> campaigns = List.of(
                new Campaign("Social Media Ads", 12000, 3000, 450),
                new Campaign("Email Campaign", 8000, 2000, 320),
                new Campaign("Google Ads", 15000, 4000, 600)
        );

        List<String> trafficLabels = List.of("Email", "Social", "Ads", "Referral");
        List<Integer> trafficData = List.of(500, 1200, 800, 300);

        List<String> dateLabels = List.of("Mon", "Tue", "Wed", "Thu", "Fri");
        List<Integer> clickCounts = List.of(100, 180, 150, 220, 300);

        model.addAttribute("campaigns", campaigns);
        model.addAttribute("trafficLabels", trafficLabels);
        model.addAttribute("trafficData", trafficData);
        model.addAttribute("dateLabels", dateLabels);
        model.addAttribute("clickCounts", clickCounts);

        model.addAttribute("event", event);
        return "marketing"; // Your Thymeleaf file name without .html
    }
}

