//package com.aremi.microservizio.security;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///***
// * E' una classe personalizzata utilizzata principalmente per gestire i casi in cui un utente non autenticato tenta di
// * accedere a una risorsa protetta
// * Quando un utente non autenticato tenta di accedere a una risorsa protetta, Spring Security chiama questo il metodo
// * "commence"
// */
//@Component
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException authException) throws IOException, ServletException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//    }
//}
