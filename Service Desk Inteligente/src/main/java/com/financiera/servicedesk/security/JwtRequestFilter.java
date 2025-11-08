package com.financiera.servicedesk.security;

// 👇 ¡LOS CAMBIOS ESTÁN AQUÍ!
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// 👆 ¡LOS CAMBIOS ESTÁN AQUÍ!

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Esto ya no debería estar en rojo
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        // 1. Extraer el token del header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                email = jwtUtil.obtenerEmail(jwt);
            } catch (Exception e) {
                // Token inválido (expirado, firma incorrecta, etc.)
                // No hacemos nada, solo dejamos que la petición continúe sin autenticación.
            }
        }

        // 2. Validar el token y establecer la autenticación
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validarToken(jwt)) {

                // Creamos la autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email, null, new ArrayList<>()); // email es el "principal"

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Le decimos a Spring Security: "Este usuario está autenticado"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 3. Continuar con el resto de los filtros
        filterChain.doFilter(request, response);
    }
}