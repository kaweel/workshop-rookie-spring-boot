package com.simple.rookie.service;

import com.simple.rookie.config.BusinessException;
import com.simple.rookie.dao.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class AuthService {

    private CustomerRepository customerRepository;

    @Autowired
    public AuthService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public SignInResponse signIn(SignInRequest request) {
        customerRepository.auth(request.getUserName(), request.getPassword())
                .orElseThrow(() -> new BusinessException(HttpStatus.FORBIDDEN, "invalid credentials"));
        return new SignInResponse(UUID.randomUUID().toString());
    }
}
