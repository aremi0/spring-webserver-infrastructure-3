package com.aremi.microservizio.exception;

import org.springframework.security.access.AccessDeniedException;

public class CustomAccessDeniedException extends AccessDeniedException {
    /***
     * Questa classe si limita a sovrascrivere il messaggio della classe madre con quello parametrizzato (contenente cioè
     * i dettagli sulle autorita) che lancia la classe 'CustomAuthorizationManager'.
     * @param msg
     */
    public CustomAccessDeniedException(String msg) {
        super(msg);
    }
}
