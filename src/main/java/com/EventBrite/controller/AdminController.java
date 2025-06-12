package com.EventBrite.controller;

import com.EventBrite.model.EventDisplay;
import com.EventBrite.model.TicketOption;
import com.EventBrite.model.User;
import com.EventBrite.repository.EventDisplayRepository;
import com.EventBrite.repository.TicketOptionRepository;
import com.EventBrite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @PreAuthorize("hasRole('USER', 'ADMIN')")
    @GetMapping("/eventManagement")
    public String eventManagement(Model model){
        model.addAttribute("event", eventDisplayRepository.findAll());
        return "eventManagement";
    }

    @PostMapping("/eventManagement/delete/{id}")
    public String deleteEvent(@PathVariable Long id){
        eventDisplayRepository.deleteById(id);
        return "redirect:/eventManagement";
    }

    @GetMapping("/UserManagement")
    public String UserManagement(Model model){
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
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