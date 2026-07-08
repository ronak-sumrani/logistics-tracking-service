package com.example.logisticstrackingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LogisticsTrackingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogisticsTrackingServiceApplication.class, args);
    }

}
