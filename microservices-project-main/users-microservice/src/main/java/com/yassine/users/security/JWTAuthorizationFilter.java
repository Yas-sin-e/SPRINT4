package com.yassine.users.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Récupérer le token de l'en-tête
        String jwt = request.getHeader("Authorization");

        // 2. Si pas de token ou ne commence pas par "Bearer ", on passe au filtre suivant
        if (jwt == null || !jwt.startsWith(SecParams.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Vérifier et décoder le token
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecParams.SECRET)).build();

        // Enlever le préfixe "Bearer " pour garder uniquement la chaîne du token
        jwt = jwt.substring(SecParams.TOKEN_PREFIX.length()).trim();

        DecodedJWT decodedJWT = verifier.verify(jwt);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);

        // 4. Convertir les rôles en autorités Spring Security
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String r : roles) {
            authorities.add(new SimpleGrantedAuthority(r));
        }

        // 5. Authentifier l'utilisateur dans le contexte de sécurité
        UsernamePasswordAuthenticationToken user =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(user);

        // 6. Continuer vers la ressource demandée
        filterChain.doFilter(request, response);
    }
}