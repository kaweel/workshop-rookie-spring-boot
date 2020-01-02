package com.simple.rookie;

import com.simple.rookie.dao.entity.Customer;
import com.simple.rookie.dao.repository.CustomerRepository;
import com.simple.rookie.service.GithubUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class RookieApplication implements CommandLineRunner {

    @Autowired
    GithubUserService githubUserService;

    @Autowired
    CustomerRepository customerRepository;

    public static void main(String[] args) {
        SpringApplication.run(RookieApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello Rookie Spring Boot!!");
        log.info("Log ma leaw ja");

        GithubUserService.User user = githubUserService.getByUserName("kaweel");
        System.out.println("Github : " + user.getName());

        Integer before = customerRepository.findAll().size();
        System.out.println("before : " + before);
        customerRepository.save(new Customer());
        Integer after = customerRepository.findAll().size();
        System.out.println("after : " + after);


    }
}
