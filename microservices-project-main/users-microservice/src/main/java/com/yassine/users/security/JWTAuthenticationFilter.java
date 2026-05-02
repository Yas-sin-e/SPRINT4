package com.yassine.users.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        try {
            com.yassine.users.entities.User user =
                    new ObjectMapper().readValue(
                            request.getInputStream(),
                            com.yassine.users.entities.User.class
                    );

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );

        } catch (IOException e) {
            throw new RuntimeException("Erreur lecture credentials", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        User springUser = (User) authResult.getPrincipal();

        List<String> roles = springUser.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());

        String jwt = JWT.create()
                .withSubject(springUser.getUsername())
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + SecParams.EXP_TIME)) // ← SecParams
                .sign(Algorithm.HMAC256(SecParams.SECRET)); // ← SecParams

        response.addHeader(SecParams.HEADER_NAME, SecParams.TOKEN_PREFIX + jwt); // ← SecParams
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {

        if (failed instanceof DisabledException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            response.setContentType("application/json");

            Map<String, Object> data = new HashMap<>();
            data.put("errorCause", "disabled");
            data.put("message", "Utilisateur désactivé !");

            PrintWriter writer = response.getWriter();
            writer.println(new ObjectMapper().writeValueAsString(data));
            writer.flush();

        } else {
            super.unsuccessfulAuthentication(request, response, failed);
        }
    }
}