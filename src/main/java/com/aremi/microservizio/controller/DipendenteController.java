package com.aremi.microservizio.controller;

import com.aremi.microservizio.dto.GenericResponse;
import com.aremi.microservizio.dto.bean.DipendenteBean;
import com.aremi.microservizio.service.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class DipendenteController {
    private final Logger logger;
    private final DipendenteService dipendenteService;

    @Autowired
    public DipendenteController(DipendenteService dipendenteService) {
        this.logger = Logger.getLogger("DipendenteController_Logger");
        this.dipendenteService = dipendenteService;
    }

    @GetMapping("/dipendente/{id}")
    GenericResponse<DipendenteBean> getDipendenteById(@PathVariable("id") Long id) {
        logger.info("RestController::getDipendenteById REST request received with ID: " + id);
        return dipendenteService.getDipendenteBeanById(id);
    }
}
