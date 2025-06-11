package com.EventBrite.controller;

import com.EventBrite.model.EventDisplay;
import com.EventBrite.model.TicketOption;
import com.EventBrite.repository.EventDisplayRepository;
import com.EventBrite.repository.TicketOptionRepository;
import com.EventBrite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @GetMapping("/AdminSettings")
    public String AdminSettings(Model model){
        return "AdminSettings";
    }
    @GetMapping("eventManagement")
    public String eventManagement(Model model){
        return "eventManagement";
    }

    @GetMapping("/UserManagement")
    public String UserManagement(Model model){
        return "UserManagement";
    }

    @GetMapping("/TicketsManagement")
    public String TicketsManagement(Model model){
        return "TicketsManagement";
    }

    @GetMapping("/AdminReport")
    public String getAdminReport(Model model) {
        List<EventDisplay> events = eventDisplayRepository.findAll();
        List<TicketOption> ticketOptions = ticketOptionRepository.findAll();

        // Build the ticketMap
        Map<Long, List<TicketOption>> ticketMap = new HashMap<>();
        for (TicketOption option : ticketOptions) {
            Long eventId = option.getEvent().getId();
            ticketMap.computeIfAbsent(eventId, k -> new ArrayList<>()).add(option);
        }

        // Build eventRevenueMap
        Map<Long, Double> eventRevenueMap = new HashMap<>();
        for (Map.Entry<Long, List<TicketOption>> entry : ticketMap.entrySet()) {
            double revenue = entry.getValue()
                    .stream()
                    .filter(t -> t.getPrice() != null)
                    .mapToDouble(TicketOption::getPrice)
                    .sum();
            eventRevenueMap.put(entry.getKey(), revenue);
        }

        model.addAttribute("events", events);
        model.addAttribute("ticketMap", ticketMap);
        model.addAttribute("eventRevenueMap", eventRevenueMap);

        return "AdminReport";
    }




}