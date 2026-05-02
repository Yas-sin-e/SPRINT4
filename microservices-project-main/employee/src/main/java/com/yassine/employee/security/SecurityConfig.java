package com.yassine.employee.security;

import java.util.Arrays;
import java.util.Collections;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    KeycloakRoleConverter keycloakRoleConverter;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (WebSecurity web) -> web.ignoring()
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .requestMatchers("/api/image/**", "/Employees/api/image/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        config.setAllowedHeaders(Arrays.asList("*"));
                        config.setAllowCredentials(true);
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                .authorizeHttpRequests(requests -> requests
                        // IMPORTANT : Les règles permitAll() EN PREMIER
                        .requestMatchers("/api/image/**", "/Employees/api/image/**").permitAll()
                        // Consulter la liste des employés : ADMIN et USER
                        .requestMatchers("/api/all").hasAnyAuthority("ADMIN", "USER")
                        // Consulter un employé par ID : ADMIN et USER
                        .requestMatchers(HttpMethod.GET, "/api/getbyid/**").hasAnyAuthority("ADMIN", "USER")
                        // Modifier un employé : ADMIN seulement
                        .requestMatchers(HttpMethod.PUT, "/api/updateemp/**").hasAuthority("ADMIN")
                        // Supprimer un employé : ADMIN seulement
                        .requestMatchers(HttpMethod.DELETE, "/api/delemp/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(rs->rs.jwt(jwt->
                        jwt.jwtAuthenticationConverter(keycloakRoleConverter)));



        return http.build();
    }
}
