package com.gyaanbot.course_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend_Url}")
    private String frontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        System.out.println("Frontend : " + frontendUrl);

        registry.addMapping("/**")
                .allowedOrigins(frontendUrl)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
