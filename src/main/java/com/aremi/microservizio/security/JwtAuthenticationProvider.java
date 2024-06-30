package com.aremi.microservizio.security;

import com.aremi.microservizio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/***
 * Un AuthenticationProvider è un componente di Spring Security che viene utilizzato per autenticare un utente.
 * Puoi creare un AuthenticationProvider personalizzato che carica i dettagli dell’utente dal tuo UserDetailsService
 * e valida il token JWT.
 */

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;

    public JwtAuthenticationProvider(@Lazy UserService userService) {
        this.userService = userService;
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

        UserDetails user = userService.loadUserByUsername(username);

        if (password.equalsIgnoreCase(user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
