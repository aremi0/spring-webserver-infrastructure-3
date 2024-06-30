package com.aremi.microservizio.security;

import com.aremi.microservizio.dto.bean.UtenteBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

/***
 * Questo filtro gestisce esclusivamente le request che arrivano all'endpoint
 * "/api/login"
 */

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final Logger logger;
    private final AuthenticationManager authManager;

    public JwtAuthenticationFilter(AuthenticationManager authManager) {
        this.authManager = authManager;
        this.logger = LoggerFactory.getLogger("JwtAuthenticationFilter_Logger");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // Deserializza il JSON della richiesta in un oggetto UtenteBean
            logger.debug("JwtAuthenticationFilter::attemptAuthentication deserializzo il JSON dalla request.");
            UtenteBean user = new ObjectMapper().readValue(request.getInputStream(), UtenteBean.class);

            // Crea un oggetto UsernamePasswordAuthenticationToken con le credenziali dell'utente
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword(), new ArrayList<>());

            // Passa l'oggetto authToken al metodo authenticate() di AuthenticationManager
            return authManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        logger.debug("JwtAuthenticationFilter::successfulAuthentication autenticazione riuscita!");
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        logger.debug("JwtAuthenticationFilter::unsuccessfulAuthentication autenticazione fallita!");
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
