package com.aremi.microservizio.security;

import com.aremi.microservizio.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/***
 * Questo filtro gestisce tutte le request con un JWT Token nell'header eccetto quelle che arrivano all'endpoint
 * "/api/login"
 *
 * Questo metodo viene chiamato per ogni richiesta HTTP. Estrae il token JWT dall’intestazione della richiesta,
 * valida il token, carica i dettagli dell’utente associato al token e imposta l’autenticazione nel SecurityContext.
 * Spring Security dovrebbe utilizzare automaticamente la classe custom JwtAuthenticationProvider quando tenta di
 * autenticare un utente.
 */

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public JwtAuthorizationFilter(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        if(Objects.isNull(header) || !header.startsWith("Bearer ")) {
            logger.warn("JWT Token does not begin with Bearer String");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwtToken = header.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

            if(!Objects.isNull(username) && jwtTokenUtil.validateToken(jwtToken, userService.loadUserByUsername(username))) {
                // Se il token è valido, aggiungi l'username al SecurityContext
                SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken(username, null));
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            logger.warn("JWT Token has expired");
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
