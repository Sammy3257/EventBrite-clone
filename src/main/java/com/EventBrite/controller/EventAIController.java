package com.EventBrite.controller;

import com.EventBrite.model.Event;
import com.EventBrite.service.AIService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class EventAIController {

    private final AIService aiService;

    @Autowired
    public EventAIController(AIService aiService) {
        this.aiService = aiService;
    }

    // Mapping for the form page
    @GetMapping("/AI")
    public String showEventForm(Model model) {
        model.addAttribute("event", new Event("", "", "", "", "", ""));
        return "createWithAI";  // This is the Thymeleaf template you would create
    }

    @PostMapping("/ai-generate")
    @ResponseBody
    public ResponseEntity<Event> generateEvent(@RequestBody(required = false) Event input) {
        try {
            // You can optionally pass some hints via `input`, or generate entirely
            Event generatedEvent = aiService.generateEvent(input);
            return ResponseEntity.ok(generatedEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    public String generateEventPage(@ModelAttribute Event eventDetails, Model model) {
        try {
            // Call the AI service to generate event data
            Event generatedEvent = aiService.generateEvent(eventDetails);

            // Add the generated event to the model to pre-populate the form
            model.addAttribute("event", generatedEvent);
            return "createWithAI";  // This is the Thymeleaf template
        } catch (Exception e) {
            // In case of error, send a default error message
            model.addAttribute("errorMessage", "Failed to generate event. Please try again.");
            return "createWithAI";
        }
    }
}
