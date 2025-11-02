package com.angular.springboot.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyAppConfig implements WebMvcConfigurer {
    @Value("${allowed.origins}")
    private String[] origins;

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    //au lieu d'ajouter le @CrossOrigin dans le controller on ecrit cette configuration
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(basePath+"/**")
                .allowedOrigins(origins);
    }
}
