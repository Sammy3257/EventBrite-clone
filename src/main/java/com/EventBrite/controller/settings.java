package com.EventBrite.controller;


import com.EventBrite.model.EventDisplay;
import com.EventBrite.repository.EventDisplayRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class settings {

    private final EventDisplayRepository eventRepository;


    public settings(EventDisplayRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/Settings")
    public String settingsView(Model model){
        EventDisplay event = eventRepository.findById(1L) // or whatever ID makes sense
                .orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event", event);
        return "Settings";
    }
}
