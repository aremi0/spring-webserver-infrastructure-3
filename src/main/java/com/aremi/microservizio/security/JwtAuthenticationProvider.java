package com.aremi.microservizio.security;

import com.aremi.microservizio.service.UtenteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/***
 * Un AuthenticationProvider è un componente di Spring Security che viene utilizzato per autenticare un utente.
 * Puoi creare un AuthenticationProvider personalizzato che carica i dettagli dell’utente dal tuo UserDetailsService
 * e valida il token JWT.
 */

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final UtenteService utenteService;
    private final Logger logger;

    public JwtAuthenticationProvider( UtenteService utenteService) {
        this.utenteService = utenteService;
        this.logger = LoggerFactory.getLogger(JwtAuthenticationProvider.class);
    }

    /***
     * Carica i dettagli dell’utente e valida il token JWT.
     * Se il token è valido, restituisce un UsernamePasswordAuthenticationToken con i dettagli dell’utente
     * e le sue autorità.
     */

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        logger.debug("JwtAuthenticationProvider::authenticate retriving user information from DB.");
        UserDetails user = utenteService.loadUserByUsername(username);

        logger.debug("JwtAuthenticationProvider::authenticate comparing password");
        if (password.equalsIgnoreCase(user.getPassword())) {
            logger.debug("JwtAuthenticationProvider::authenticate success! logging in...");
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        } else {
            logger.debug("JwtAuthenticationProvider::authenticate failed! wrong password.");
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
