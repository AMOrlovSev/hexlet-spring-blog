package io.hexlet.spring.daytime;

import jakarta.annotation.PostConstruct;

public class Night implements Daytime {
    private String name = "night";

    public String getName() {
        return name;
    }

    @PostConstruct
    public void init() {
        var message = "\nBean Night is initialized!\n";
        System.out.println(message);
    }
}
