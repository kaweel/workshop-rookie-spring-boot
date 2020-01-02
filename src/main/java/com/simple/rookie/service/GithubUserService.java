package com.simple.rookie.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Service
public class GithubUserService {

    @Value("${rest.github.get-user}")
    private String url;

    private RestTemplate restTemplate;

    @Autowired
    public GithubUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User getByUserName(String userName) throws URISyntaxException {
        URI uri = new URI(url + userName);
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(uri, User.class);
        return responseEntity.getBody();
    }


    @Data
    public static class User {
        @JsonProperty("login")
        private String login;
        @JsonProperty("name")
        private String name;
        @JsonProperty("blog")
        private String blog;
    }
}
