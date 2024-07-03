package com.aremi.microservizio.controller;

import com.aremi.microservizio.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class JWTAuthenticationController {
    private final Logger logger;
    private final UtenteService utenteService;

    @Autowired
    public JWTAuthenticationController(UtenteService utenteService) {
        this.logger = Logger.getLogger("JWTAuthenticationController_Logger");
        this.utenteService = utenteService;
    }
}
