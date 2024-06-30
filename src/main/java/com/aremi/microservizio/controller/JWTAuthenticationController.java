package com.aremi.microservizio.controller;

import com.aremi.microservizio.dto.GenericResponse;
import com.aremi.microservizio.dto.bean.UtenteBean;
import com.aremi.microservizio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class JWTAuthenticationController {
    private final Logger logger;
    private final UserService userService;

    @Autowired
    public JWTAuthenticationController(UserService userService) {
        this.logger = Logger.getLogger("JWTAuthenticationController_Logger");
        this.userService = userService;
    }
}
