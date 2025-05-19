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
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public AuthController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            // Not logged in, redirect to login page or show error
            return "redirect:/login";
        }

        String email;

        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            // Local form login
            email = userDetails.getUsername();

        } else if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            // Google OAuth2 login
            email = oidcUser.getEmail();

        } else {
            // Unknown principal type - handle gracefully
            // For example, redirect to login or show error page
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);

                    // Set a dummy password to satisfy non-null constraint
                    newUser.setPassword("OAUTH_USER_" + UUID.randomUUID());

                    // Optional: set default role and username
                    newUser.setRole("ROLE_USER");
                    newUser.setUsername(email);  // or derive username differently

                    return userRepository.save(newUser);
                });


        model.addAttribute("user", user);
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


}
