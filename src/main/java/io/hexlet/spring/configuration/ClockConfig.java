package io.hexlet.spring.configuration;

import io.hexlet.spring.daytime.Day;
import io.hexlet.spring.daytime.Daytime;
import io.hexlet.spring.daytime.Night;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Configuration
public class ClockConfig {
    // Бин Clock: либо системное время, либо фиксированное из application.yml
    @Bean
    public Clock clock(@Value("${app.fixedTime:}") String fixedTime) {
        if (!fixedTime.isEmpty()) {
            return Clock.fixed(
                    LocalDateTime.parse(fixedTime)
                            .atZone(ZoneId.systemDefault())
                            .toInstant(),
                    ZoneId.systemDefault()
            );
        }
        return Clock.systemDefaultZone();
    }

    // BEGIN (write your solution here)
    @Bean
    @RequestScope
    public Daytime daytime(Clock clock) {
        int hour = LocalDateTime.now(clock).getHour();
        if (hour >= 6 && hour < 22) {
            return new Day();
        } else {
            return new Night();
        }
    }
    // END
}
