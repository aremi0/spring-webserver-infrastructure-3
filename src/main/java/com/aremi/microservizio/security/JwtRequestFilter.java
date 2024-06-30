//package com.aremi.microservizio.security;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Objects;
//
///***
// * JwtRequestFilter intercetta ogni richiesta HTTP, estrae il token JWT dall’intestazione della richiesta, valida il token,
// * carica i dettagli dell’utente associato al token e imposta l’autenticazione nel SecurityContext.
// *
// */
//
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter {
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    /***
//     * Questo metodo viene chiamato per ogni richiesta HTTP. Estrae il token JWT dall’intestazione della richiesta,
//     * valida il token, carica i dettagli dell’utente associato al token e imposta l’autenticazione nel SecurityContext.
//     *
//     * Spring Security dovrebbe utilizzare automaticamente la classe custom JwtAuthenticationProvider quando tenta di
//     * autenticare un utente
//     */
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        final String requestTokenHeader = request.getHeader("Authorization");
//
//        String username = null;
//        String jwtToken = null;
//        if(!Objects.isNull(requestTokenHeader) && requestTokenHeader.startsWith("Bearer ")) {
//            jwtToken = requestTokenHeader.substring(7);
//
//            try {
//                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//            } catch (IllegalArgumentException e) {
//                System.out.println("Unable to get JWT Token");
//            } catch (ExpiredJwtException e) {
//                System.out.println("JWT Token has expired");
//            }
//        } else {
//            logger.warn("JWT Token does not begin with Bearer String");
//        }
//
//        if(!Objects.isNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
//            // Se il token è valido, aggiungi l'username al SecurityContext
//            // Spring Security utilizzerà automaticamente il mio JwtAuthenticationProvider per caricare i dettagli dell’utente e validare il token JWT.
//            SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken(username, null));
//        }
//        filterChain.doFilter(request, response);
//    }
//}
