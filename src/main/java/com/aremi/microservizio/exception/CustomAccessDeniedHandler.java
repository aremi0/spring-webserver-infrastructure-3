package com.aremi.microservizio.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.debug("CustomAccessDeniedHandler::handle started... eccezione custom 'CustomAccessDeniedException' intercettata.");

        logger.debug("CustomAccessDeniedHandler::handle inserisco header Description");
        // Aggiungi un header personalizzato
        response.addHeader("Description", "Accesso negato: non hai l'autorita' necessaria per accedere a questa risorsa.");

        logger.debug("CustomAccessDeniedHandler::handle scrivo Message nel body della response e setto http a 401");
        // Modifica il body della risposta
        response.getWriter().write(accessDeniedException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
