package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EwmService {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static void main(String[] args) {
        SpringApplication.run(EwmService.class, args);
    }

}