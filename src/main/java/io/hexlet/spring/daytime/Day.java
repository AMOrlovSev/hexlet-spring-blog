package io.hexlet.spring.daytime;

import jakarta.annotation.PostConstruct;

public class Day implements Daytime {
    private String name = "day";

    public String getName() {
        return name;
    }

    // BEGIN (write your solution here)
    @PostConstruct
    public void init() {
        var message = "\nBean Day is initialized!\n";
        System.out.println(message);
    }
    // END
}

