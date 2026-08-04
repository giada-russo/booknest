package it.polimi.booknest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configura la politica CORS per gli endpoint REST dell'applicazione.
 * <p>
 * Il client Flutter è servito su un'origine diversa da quella del backend
 * (porte distinte), quindi il browser bloccherebbe le richieste in assenza
 * di un'autorizzazione esplicita da parte del server.
 * Vengono abilitati i soli percorsi sotto {@code /api} e l'origine del client.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5001")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}

