package com.addplus.server.scheduled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.addplus.server")
@EnableScheduling
public class ScheduledApplication {

    public static void main(String[] args) {

        SpringApplication.run(ScheduledApplication.class, args);
    }

}
