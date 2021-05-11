package com.example.tgpmsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8088")
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH","DELETE")
                .allowedHeaders("*")
                //cookie设置
                .allowCredentials(true).maxAge(3600);
    }

}
