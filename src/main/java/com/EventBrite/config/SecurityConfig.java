package com.EventBrite.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain attendeeSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/Attendee-login", "/Attendee-Home", "/attendee/**", "/attendee-logout") // include logout here
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/Attendee-login", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().hasRole("USER")
                )
                .formLogin(form -> form
                        .loginPage("/Attendee-login")
                        .loginProcessingUrl("/Attendee-login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/Attendee-Home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/attendee-logout", "GET"))
                        .logoutSuccessUrl("/Attendee-login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }


    // 2. General Login Configuration
    @Bean
    @Order(2)
    public SecurityFilterChain userSecurity(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/signup", "/login", "/css/**", "/js/**", "/images/**", "/oauth2/**").permitAll()
                        .requestMatchers("/home", "/Event", "/AI", "/ai-generate").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(generalUserSuccessHandler()) // 👈 custom success
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(generalUserSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }

    // Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Redirect after general user login
    @Bean
    public AuthenticationSuccessHandler generalUserSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) ->
                response.sendRedirect("/home");
    }

    // ✅ Redirect after attendee login
    @Bean
    public AuthenticationSuccessHandler attendeeSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) ->
                response.sendRedirect("/Attendee-Home");
    }
}
