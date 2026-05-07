package iskolarium_backend.config;

import iskolarium_backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // 1. ALLOW ALL OPTIONS REQUESTS (This fixes the 403 for POST requests)
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
            
            // 2. Allow public access to login/register and searching
            .requestMatchers("/api/users/register", "/api/users/login").permitAll()
            .requestMatchers("/api/scholarships/search").permitAll()
            
            // 3. Require authentication for everything else
            .anyRequest().authenticated()
        )
        // 4. NEW SYNTAX: Lambda style for Session Management
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        // 5. CRITICAL: Add your JWT filter so it actually checks the tokens!
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
    return http.build(); }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

