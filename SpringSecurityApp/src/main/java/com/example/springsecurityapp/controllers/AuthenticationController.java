package com.example.springsecurityapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
    @GetMapping("/authentication")
    public String login(){
        return "authentication";
    }

}

// Данный класс создан чтобы показать что могут быть реализованы одновременно несколько классов-контроллеров. В принципе это не обязательно.