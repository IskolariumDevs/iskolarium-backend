package iskolarium_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Global CORS configuration.
 *
 * Allowed origins are set via the FRONTEND_URL environment variable on Render.
 * Add it as:   FRONTEND_URL = https://iskolarium-app.onrender.com
 *
 * For local development it falls back to http://localhost:3000 and
 * the file:// origin used when opening HTML files directly.
 */
@Configuration
public class WebConfig {

    /**
     * Read the production frontend URL from an environment variable.
     * If not set, defaults to localhost for local development.
     */
    @Value("${FRONTEND_URL:http://localhost:3000}")
    private String frontendUrl;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // ── Allowed Origins ──────────────────────────────────────────────────
        // Add your Render static-site URL + common local dev origins.
        // The FRONTEND_URL env-var covers the production URL without
        // hard-coding it here.
        config.setAllowedOrigins(List.of(
            frontendUrl,                          // from FRONTEND_URL env var
            "http://localhost:3000",              // local dev server
            "http://127.0.0.1:3000",             // alternate local
            "http://localhost:5500",              // VS Code Live Server
            "http://127.0.0.1:5500"              // VS Code Live Server (127)
        ));

        // ── Allowed Methods ───────────────────────────────────────────────────
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // ── Allowed Headers ───────────────────────────────────────────────────
        config.setAllowedHeaders(List.of("*"));

        // ── Expose Authorization response header to the frontend ──────────────
        config.setExposedHeaders(List.of("Authorization"));

        // ── Allow credentials (cookies / Authorization header) ────────────────
        config.setAllowCredentials(true);

        // ── Apply to every endpoint ───────────────────────────────────────────
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
