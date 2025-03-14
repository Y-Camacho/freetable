package com.camacho.proyecto.freetable.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientController {

    @GetMapping("/dashboard")
    public String clientDashboard() {
        return "client/dashboard";
    }
}