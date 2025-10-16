package org.example.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI aoemOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("AOEM EV Warranty Management API")
                                                .description("""
                                                                API documentation for AOEM EV Warranty Management System

                                                                This API provides endpoints for managing:
                                                                - User authentication and authorization
                                                                - Vehicle warranty claims
                                                                - Service recalls
                                                                - Parts catalog and inventory
                                                                - Service center management
                                                                - OEM manufacturer operations

                                                                ## Authentication
                                                                Most endpoints require authentication. Use the login endpoint to obtain a session token.

                                                                ## Rate Limiting
                                                                API calls are rate limited. Please respect the rate limits to ensure system stability.
                                                                """)
                                                .version("v1.0")
                                                .contact(new Contact()
                                                                .name("AOEM Development Team")
                                                                .email("support@aoem.com")
                                                                .url("https://aoem.com"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                                .servers(List.of(
                                                new Server().url("http://localhost:8080")
                                                                .description("Local Development Server"),
                                                new Server().url("https://api.aoem.com")
                                                                .description("Production Server")))
                                .tags(List.of(
                                                new Tag().name("Authentication").description(
                                                                "User authentication and session management"),
                                                new Tag().name("Users").description("User management operations"),
                                                new Tag().name("Vehicles")
                                                                .description("Vehicle registration and management"),
                                                new Tag().name("Warranty Claims")
                                                                .description("Warranty claim processing"),
                                                new Tag().name("Recalls")
                                                                .description("Service recalls"),
                                                new Tag().name("Parts").description("Parts catalog and inventory"),
                                                new Tag().name("Service Centers")
                                                                .description("Service center management"),
                                                new Tag().name("OEM").description("OEM manufacturer operations"),
                                                new Tag().name("Roles").description("Role and permission management")))
                                .externalDocs(new ExternalDocumentation()
                                                .description("AOEM Project Documentation")
                                                .url("https://docs.aoem.com"))
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")
                                                                .description("JWT token obtained from login endpoint")));
        }

        @Bean
        public org.springdoc.core.models.GroupedOpenApi publicApi() {
                return org.springdoc.core.models.GroupedOpenApi.builder()
                                .group("aoem-public")
                                .pathsToMatch("/api/**")
                                .build();
        }
}
