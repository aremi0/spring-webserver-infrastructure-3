package com.aremi.microservizio.controller;

import com.aremi.microservizio.dto.GenericResponse;
import com.aremi.microservizio.dto.bean.UtenteBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JWTAuthenticationController {
    @PostMapping("/authorize")
    GenericResponse<Void> authorize(@RequestBody UtenteBean user) {

    }
}
