package com.online_store.configuration;

import com.online_store.constants.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Component
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        final var corsConfiguration = new CorsConfiguration();
        final var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        corsConfiguration.setAllowedOrigins(Collections.singletonList(Path.FRONT_END_LOCALHOST));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setAllowCredentials(true);
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }
}
