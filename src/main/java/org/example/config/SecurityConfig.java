package org.example.config;

import java.util.List;

import org.example.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Autowired
        private JsonAuthenticationEntryPoint jsonAuthenticationEntryPoint;

        @Autowired
        private JsonAccessDeniedHandler jsonAccessDeniedHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> {
                                })
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(jsonAuthenticationEntryPoint)
                                                .accessDeniedHandler(jsonAccessDeniedHandler))
                                .authorizeHttpRequests(auth -> auth
                                                // Public docs and health
                                                .requestMatchers(
                                                                "/api-docs/**",
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/actuator/**")
                                                .permitAll()
                                                // Auth endpoints
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/auth/login")
                                                .permitAll()
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/auth/me")
                                                .authenticated()
                                                // Users - Admin only
                                                .requestMatchers("/api/users/**").hasRole("Admin")
                                                // Service Centers
                                                .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                                                "/api/service-centers/**")
                                                .hasRole("Admin")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/service-centers/**")
                                                .hasAnyRole("Admin", "SC_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                                                "/api/service-centers/**")
                                                .hasAnyRole("Admin", "SC_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/service-centers/**")
                                                .hasAnyRole("Admin", "SC_Staff", "EVM_Staff")
                                                // Vehicles
                                                .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                                                "/api/vehicles/**")
                                                .hasRole("Admin")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/vehicles/**")
                                                .hasAnyRole("Admin", "SC_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                                                "/api/vehicles/**")
                                                .hasAnyRole("Admin", "SC_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/vehicles")
                                                .hasAnyRole("Admin", "SC_Staff", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/vehicles/**")
                                                .hasAnyRole("Admin", "SC_Staff", "SC_Technician", "EVM_Staff")
                                                // Warranty Claims
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/claims/**")
                                                .hasAnyRole("Admin", "SC_Staff", "SC_Technician", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/claims")
                                                .hasAnyRole("Admin", "SC_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                                                "/api/claims/**")
                                                .hasAnyRole("Admin", "SC_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                                                "/api/claims/**")
                                                .hasRole("Admin")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/claims/*/submit")
                                                .hasAnyRole("Admin", "SC_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/claims/*/assign")
                                                .hasAnyRole("Admin", "SC_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/claims/*/approve")
                                                .hasAnyRole("Admin", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/claims/*/reject")
                                                .hasAnyRole("Admin", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/claims/*/cancel")
                                                .hasAnyRole("Admin", "SC_Staff")
                                                // Service Records
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/service-records/**")
                                                .hasAnyRole("Admin", "SC_Staff", "SC_Technician", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/service-records/**")
                                                .hasAnyRole("Admin", "SC_Staff", "SC_Technician")
                                                .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                                                "/api/service-records/**")
                                                .hasRole("Admin")
                                                // Inventory
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/inventory/**")
                                                .hasAnyRole("Admin", "SC_Staff", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/inventory/parts/*/reserve")
                                                .hasAnyRole("Admin", "SC_Staff", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/inventory/parts/*/release")
                                                .hasAnyRole("Admin", "SC_Staff", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/inventory/parts")
                                                .hasRole("EVM_Staff")
                                                // Shipments
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/shipments/**")
                                                .hasAnyRole("Admin", "SC_Staff", "EVM_Staff")
                                                // deliver must be before general POST matcher
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/shipments/*/deliver")
                                                .hasAnyRole("Admin", "SC_Staff", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/shipments/**")
                                                .hasAnyRole("Admin", "EVM_Staff")
                                                // Warranty Policies
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/warranty-policies/**")
                                                .hasAnyRole("Admin", "SC_Staff", "SC_Technician", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/warranty-policies/**")
                                                .hasAnyRole("Admin", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                                                "/api/warranty-policies/**")
                                                .hasAnyRole("Admin", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                                                "/api/warranty-policies/**")
                                                .hasAnyRole("Admin", "EVM_Staff")
                                                // Parts catalog
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/parts/**")
                                                .hasAnyRole("Admin", "SC_Staff", "SC_Technician", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/parts/**")
                                                .hasAnyRole("Admin", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                                                "/api/parts/**")
                                                .hasAnyRole("Admin", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                                                "/api/parts/**")
                                                .hasAnyRole("Admin", "EVM_Staff")
                                                // Reports
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/reports/performance/**")
                                                .hasAnyRole("Admin", "SC_Staff", "SC_Technician", "EVM_Staff")
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/reports/**")
                                                .hasAnyRole("Admin", "SC_Staff", "EVM_Staff")
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With",
                                "ngrok-skip-browser-warning"));
                configuration.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
