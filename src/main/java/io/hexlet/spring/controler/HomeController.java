package io.hexlet.spring.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Добро пожаловать в Hexlet Spring Blog!";
    }
}
