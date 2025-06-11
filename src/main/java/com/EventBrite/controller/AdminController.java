package com.EventBrite.controller;

import com.EventBrite.model.EventDisplay;
import com.EventBrite.repository.EventDisplayRepository;
import com.EventBrite.repository.TicketOptionRepository;
import com.EventBrite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {


    @Autowired
    private EventDisplayRepository eventDisplayRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketOptionRepository ticketOptionRepository;

    @GetMapping("/Admin")
    public String adminPage(Model model){

        long registerUsers = userRepository.count();
        model.addAttribute("registerUsers", registerUsers);

        long eventCount = eventDisplayRepository.count();
        model.addAttribute("eventCount", eventCount);

        long soldTickets = ticketOptionRepository.count();
        model.addAttribute("soldTickets", soldTickets);

        return "Admin";
    }

}
