package com.aremi.microservizio.security;

import com.aremi.microservizio.exception.CustomAccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final RequestMatcher protectedResource = new AntPathRequestMatcher("/api/dipendentiBySede/**");
    private final String requiredAuthority = "superuser";
    private Logger logger = LoggerFactory.getLogger(CustomAuthorizationManager.class);
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext  context) {
        Authentication auth = authentication.get();
        HttpServletRequest request = context.getRequest();

        logger.debug("CustomAuthorizationManager::check listing user authorities: " + auth.getAuthorities());

        if(!protectedResource.matches(request)) {
            // Se la risorsa non è protetta, concedi l'accesso
            logger.debug("CustomAuthorizationManager::check la risorsa non è protetta, ingresso permesso:\n" + request);
            return new AuthorizationDecision(true);
        }

        // Controlla se l'utente ha l'autorità richiesta
        boolean hasAuthority = auth.getAuthorities().stream()
                .anyMatch(userAuthorities -> requiredAuthority.equalsIgnoreCase(userAuthorities.getAuthority()));

        if(!hasAuthority) {
            // Se l'utente non ha l'autorità richiesta, nega l'accesso e lancia un'eccezione
            logger.debug("CustomAuthorizationManager::check l'utente non ha l'autorita richiesta, lancio eccezione custom");

            throw new CustomAccessDeniedException("Accesso negato: per accedere a questa risorsa, e' necessaria l'autorita' {"
                    + requiredAuthority + "}. Le tue autorita' sono: {" + auth.getAuthorities() + "}.");

        }

        // Altrimenti, concedi l'accesso
        logger.debug("CustomAuthorizationManager::check Permission granted, resource unlocked");
        return new AuthorizationDecision(true);
    }
}
