package com.example.productcrud.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Mengizinkan semua path di aplikasi
                        .allowedOrigins("*") // Mengizinkan semua domain
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Mengizinkan metode HTTP tertentu
                        .allowedHeaders("*"); // Mengizinkan semua header
            }
        };
    }
}
