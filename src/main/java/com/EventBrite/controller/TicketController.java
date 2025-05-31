package com.EventBrite.controller;

import com.EventBrite.model.EventDisplay;
import com.EventBrite.model.TicketOption;
import com.EventBrite.repository.EventDisplayRepository;
import com.EventBrite.repository.TicketOptionRepository;  // <- Add this import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TicketController {

    private final EventDisplayRepository eventRepository;
    private final TicketOptionRepository ticketOptionRepository;

    public TicketController(EventDisplayRepository eventRepository, TicketOptionRepository ticketOptionRepository) {
        this.eventRepository = eventRepository;
        this.ticketOptionRepository = ticketOptionRepository;
    }

    @GetMapping("/ticket/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        EventDisplay event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<TicketOption> ticketOptions = ticketOptionRepository.findByEvent(event);

        model.addAttribute("event", event);
        model.addAttribute("ticketOptions", ticketOptions);
        return "ticket";
    }
}
