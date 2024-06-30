package com.aremi.microservizio.security;

import com.aremi.microservizio.service.UtenteService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
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
    private final UtenteService utenteService;
    private Logger logger;

    public JwtAuthorizationFilter(JwtTokenUtil jwtTokenUtil, UtenteService utenteService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.utenteService = utenteService;
        this.logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        logger.debug("JwtAuthorization::doFilterInternal started, extracting Authorization header from request");

        final String header = request.getHeader("Authorization");

        if(Objects.isNull(header) || !header.startsWith("Bearer ")) {
            logger.warn("JwtAuthorization::doFilterInternal JWT Token does not begin with Bearer String");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwtToken = header.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            logger.debug("JwtAuthorization::doFilterInternal ottenuto username dal token: " + username);

            UserDetails userDetails = utenteService.loadUserByUsername(username);

            if(!Objects.isNull(username) && jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                // Se il token è valido, aggiungi l'username al SecurityContext
                logger.debug("JwtAuthorization::doFilterInternal il token è valido, inserisco utente nel SecurityContext.");

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, userDetails.getAuthorities()); // Le authorities() si riferiscono alle autorita prese dalla colonna "autorita" nella tabella "Utente"

                SecurityContextHolder.getContext().setAuthentication(authentication);
                onAuthenticationSuccess(jwtToken, response);
            } else {
                logger.warn("JwtAuthorization::doFilterInternal il token non valido o validazione non riuscita");
            }
        } catch (IllegalArgumentException e) {
            logger.warn("JwtAuthorization::doFilterInternal Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            logger.warn("JwtAuthorization::doFilterInternal JWT Token has expired");
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    protected void onAuthenticationSuccess(String jwtToken, HttpServletResponse response) {
        logger.debug("JwtAuthorization::onAuthenticationSuccess l'autenticazione del login ha avuto successo!\nInserisco jwtToken e ExpirationDate in response header.");
        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(jwtToken);

        // Invia il token nella risposta HTTP
        response.addHeader("Authorization", "Bearer " + jwtToken);
        response.addHeader("JWT-Expiration-Date", expirationDate.toString());
    }
}
