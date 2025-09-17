package org.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        System.out.println("\nAplicación iniciada correctamente!");
        System.out.println("Los endpoints están disponibles en:");
        System.out.println("- http://localhost:8081/api/recepciones");
        System.out.println("- http://localhost:8081/api/vigilancias");
    }


}
