package com.financiera.servicedesk.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig { // ¡Ya no extendemos WebSecurityConfigurerAdapter!

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf(csrf -> csrf.disable()) // Nueva sintaxis para deshabilitar CSRF

                .authorizeHttpRequests(authz -> authz
                        // 1. Define tus rutas PÚBLICAS aquí
                        // ¡La sintaxis cambió de antMatchers a requestMatchers!
                        .requestMatchers("/api/colaboradores/login").permitAll()
                        .requestMatchers("/api/colaboradores/forgot-password").permitAll()
                        .requestMatchers("/api/colaboradores/reset-password").permitAll()

                        // 2. Define tus rutas PRIVADAS (todas las demás)
                        .requestMatchers("/api/colaboradores/**").authenticated() // Protege /api/colaboradores, /api/colaboradores/1, etc.
                        .anyRequest().authenticated() // Protege CUALQUIER otra ruta que no hayamos listado
                )

                // 3. Le decimos a Spring que no cree sesiones (porque usamos JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // 4. Le decimos a Spring que use NUESTRO filtro (JwtRequestFilter)
        // ANTES del filtro de login por defecto.
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // 5. Construimos la cadena de filtros
        return http.build();
    }
}