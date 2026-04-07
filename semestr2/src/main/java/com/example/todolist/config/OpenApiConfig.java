package com.example.todolist.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) configuration for the Spring Boot application.
 * Provides API documentation and Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("To-Do List Manager API")
                        .version("2.0.0")
                        .description("Advanced To-Do List Manager with DTOs, validation, file uploads, and session management")
                        .contact(new Contact()
                                .name("Development Team")
                                .url("https://example.com")
                                .email("dev@example.com")))
                .components(new Components()
                        .addSecuritySchemes("session", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("JSESSIONID")))
                .addSecurityItem(new SecurityRequirement().addList("session"));
    }
}
