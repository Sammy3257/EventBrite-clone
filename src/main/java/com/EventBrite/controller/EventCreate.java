package com.EventBrite.controller;

import com.EventBrite.model.User;
import com.EventBrite.model.EventDisplay;
import com.EventBrite.repository.EventDisplayRepository;
import com.EventBrite.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
public class EventCreate {

    private final UserRepository userRepository;
    private final EventDisplayRepository eventRepository;
    private final Path rootLocation = Paths.get("uploads");

    @Autowired
    public EventCreate(UserRepository userRepository,
                       EventDisplayRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }



    @GetMapping("/manual")
    public String showCreateForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof DefaultOidcUser) {
            email = ((DefaultOidcUser) principal).getEmail();
        } else {
            throw new RuntimeException("Unsupported user principal type: " + principal.getClass().getName());
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("event", new EventDisplay());
        return "createEvent";
    }




    @GetMapping("/event/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        EventDisplay event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        model.addAttribute("uploadsPath", "/uploads/");
        if (event.getPriceVip() == null) event.setPriceVip(0.0);
        if (event.getPriceRegular() == null) event.setPriceRegular(0.0);
        if (event.getPricePopular() == null) event.setPricePopular(0.0);
        if (event.getPriceSingle() == null) event.setPriceSingle(0.0);
        model.addAttribute("event", event);
        model.addAttribute("_csrf",
                RequestContextHolder.getRequestAttributes().getAttribute("_csrf", 0));
        return "eventDisplay";
    }

    @PostMapping("/manual")
    public String handleEventCreation(
            @Valid @ModelAttribute("event") EventDisplay event,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("videoFile") MultipartFile videoFile,
            @RequestParam(value = "isPaid", required = false) String isPaidStr,
            @RequestParam(value = "paidType", required = false) String paidTypeStr,
            @RequestParam(value = "priceVip", required = false) Double priceVip,
            @RequestParam(value = "priceRegular", required = false) Double priceRegular,
            @RequestParam(value = "pricePopular", required = false) Double pricePopular,
            @RequestParam(value = "priceSingle", required = false) Double priceSingle,
            Model model) throws IOException {

        if (result.hasErrors()) {
            return "createEvent";
        }

        // Convert string to boolean for isPaid
        event.setIsPaid("true".equalsIgnoreCase(isPaidStr));

        // Convert paidType string to enum, if present
        if (paidTypeStr != null && !paidTypeStr.isEmpty()) {
            try {
                event.setPaidType(EventDisplay.TicketType.valueOf(paidTypeStr));
            } catch (IllegalArgumentException e) {
                event.setPaidType(null);
            }
        } else {
            event.setPaidType(null);
        }

        event.setPriceVip(priceVip);
        event.setPriceRegular(priceRegular);
        event.setPricePopular(pricePopular);
        event.setPriceSingle(priceSingle);

        // Handle file uploads as you already do
        if (!imageFile.isEmpty()) {
            String imagePath = saveFile(imageFile, "images");
            event.setImagePath(imagePath);
        }

        if (!videoFile.isEmpty()) {
            String videoPath = saveFile(videoFile, "videos");
            event.setVideoPath(videoPath);
        }

        EventDisplay savedEvent = eventRepository.save(event);
        return "redirect:/event/" + savedEvent.getId();
    }



    @DeleteMapping("/event/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }
        eventRepository.deleteById(id);
        return ResponseEntity.ok("Event deleted successfully");
    }

    private String saveFile(MultipartFile file, String subDir) throws IOException {
        Path targetLocation = this.rootLocation.resolve(subDir);
        Files.createDirectories(targetLocation);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path destination = targetLocation.resolve(filename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return subDir + "/" + filename;
    }
}