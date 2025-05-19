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
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("event", new EventDisplay());
        return "createEvent";
    }

    @PostMapping("/manual")
    public String handleEventCreation(
            @Valid @ModelAttribute("event") EventDisplay event,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("videoFile") MultipartFile videoFile,
            Model model) throws IOException {

        if (result.hasErrors()) {
            return "createEvent";
        }

        // Handle file uploads
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

    @GetMapping("/event/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        EventDisplay event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        model.addAttribute("uploadsPath", "/uploads/");
        model.addAttribute("event", event);
        model.addAttribute("_csrf",
                RequestContextHolder.getRequestAttributes().getAttribute("_csrf", 0));
        return "event";
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