package com.EventBrite.controller;

import com.EventBrite.model.EventDisplay;
import com.EventBrite.model.User;
import com.EventBrite.repository.EventDisplayRepository;
import com.EventBrite.repository.UserRepository;
import com.EventBrite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
public class AuthController {

    private final EventDisplayRepository eventRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public AuthController(EventDisplayRepository eventRepository, UserRepository userRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") User user, Model model) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "signup";
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already registered");
            return "signup";
        }

        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        EventDisplay event = (EventDisplay) eventRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No events found"));


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        String email;

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            // Local form login
            email = userDetails.getUsername();

        } else if (principal instanceof DefaultOidcUser oidcUser) {
            // Google OAuth2 login
            email = oidcUser.getAttribute("email");

        } else {
            return "redirect:/login";
        }


        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setPassword(passwordEncoder.encode("oauth_placeholder_password"));
                    newUser.setRole("ROLE_USER");
                    newUser.setUsername(email);
                    return userRepository.save(newUser);
                });

        model.addAttribute("user", user);
        model.addAttribute("event", event);
        return "home";
    }




    @Autowired
    private EventDisplayRepository eventDisplayRepository;


    @GetMapping("/Event")
    public String event(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        List<EventDisplay> events = eventDisplayRepository.findAll();
        model.addAttribute("events", events);

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);

        return "event";
    }


    @GetMapping("/reports/{id}")
    public String reports(@PathVariable Long id, Model model) {
        EventDisplay event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        model.addAttribute("event", event);
        return "reports";
    }



}
