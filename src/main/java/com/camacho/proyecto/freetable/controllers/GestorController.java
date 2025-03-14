package com.camacho.proyecto.freetable.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gestor")
public class GestorController {

    @GetMapping("/dashboard")
    public String gestorDashboard() {
        return "gestor/dashboard";
    }
}