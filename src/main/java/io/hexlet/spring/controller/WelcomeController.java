package io.hexlet.spring.controller;

import io.hexlet.spring.daytime.Daytime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @Autowired
    private Daytime daytime;

    @GetMapping(path = "/welc")
    public String welcome() {
        return "It is " + daytime.getName() + " now! Welcome to Spring!";
    }
}