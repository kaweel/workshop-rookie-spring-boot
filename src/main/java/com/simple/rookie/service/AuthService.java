package com.simple.rookie.service;

import com.simple.rookie.config.BusinessException;
import com.simple.rookie.controller.request.SignInRequest;
import com.simple.rookie.controller.response.SignInResponse;
import com.simple.rookie.dao.entity.Customer;
import com.simple.rookie.dao.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class AuthService {

    private Map<String, String> tokenMap = new HashMap<>();

    private CustomerRepository customerRepository;

    public Boolean tokenIsValid(String token) {
        if (null == token) {
            return false;
        }
        return tokenMap.containsValue(token);
    }

    @Autowired
    public AuthService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public SignInResponse signIn(SignInRequest request) {
        Customer customer = customerRepository.auth(request.getUsername(), request.getPassword())
                .orElseThrow(() -> new BusinessException(HttpStatus.FORBIDDEN, "invalid credentials"));

        String token = UUID.randomUUID().toString();

        tokenMap.put(customer.getUserName(), token);

        return new SignInResponse(token);
    }
}
