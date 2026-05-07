package iskolarium_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to all endpoints
                .allowedOrigins("*") // Allow all frontends (change to your Render URL later for security)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // EXPLICITLY allow the OPTIONS preflight scout!
                .allowedHeaders("*"); // Allow all headers like Authorization
    }
}