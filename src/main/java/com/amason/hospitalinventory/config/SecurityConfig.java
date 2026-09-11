package com.amason.hospitalinventory.config;

import com.amason.hospitalinventory.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // Our custom filter from the previous step - Spring will run this 
    // on every request, before checking any of the rules below
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())

            // Tells Spring: "don't create browser-style login sessions - 
            // every request proves itself fresh with its own token, 
            // nothing is remembered between requests on the server side"
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                // Login itself must be reachable by anyone, logged in or not
                .requestMatchers("/api/auth/login").permitAll()

                // Only ADMIN can create new users (no self-registration) 
                // or manage suppliers/departments
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/users").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/suppliers").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/departments").hasRole("ADMIN")

                // ADMIN or STOREKEEPER can create products and record stock movements
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/products").hasAnyRole("ADMIN", "STOREKEEPER")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/stock-movements").hasAnyRole("ADMIN", "STOREKEEPER")

                // Everything else requires SOME valid login, any role
                .anyRequest().authenticated()
            )

            // Plug our custom filter in, telling it to run BEFORE 
            // Spring's own built-in login filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
