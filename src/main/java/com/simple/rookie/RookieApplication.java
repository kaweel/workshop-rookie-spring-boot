package com.simple.rookie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class RookieApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RookieApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello Rookie Spring Boot!!");
        log.info("Log ma leaw ja");

    }
}
