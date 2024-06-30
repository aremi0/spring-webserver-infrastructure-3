package com.aremi.microservizio.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenUtil jwtTokenUtil;
    private Logger logger;

    public JwtAuthenticationSuccessHandler(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.logger = LoggerFactory.getLogger(JwtAuthenticationSuccessHandler.class);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // Genera un token JWT per l'utente
        logger.debug("JwtAuthenticationSuccessHandler::onAuthenticationSuccess l'autenticazione del login ha avuto successo, genero nuovo JWT per l'utente.");
        String token = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);

        // Invia il token nella risposta HTTP
        logger.debug("JwtAuthenticationSuccessHandler::onAuthenticationSuccess inserisco il JWT nell'header della risposta.");
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("JWT-Expiration-Date", expirationDate.toString());
    }
}
