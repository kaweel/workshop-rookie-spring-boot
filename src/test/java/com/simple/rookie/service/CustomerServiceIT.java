package com.simple.rookie.service;

import com.simple.rookie.controller.request.CreateCustomerRequest;
import com.simple.rookie.controller.request.SignInRequest;
import com.simple.rookie.controller.response.SignInResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "it")
public class CustomerServiceIT {

    @Autowired
    AuthService authService;

    @Autowired
    CustomerService customerService;

    @Test
    public void found() {

        SignInRequest request = new SignInRequest();
        request.setUsername("ink");
        request.setPassword("1234");
        SignInResponse signInResponse = authService.signIn(request);

        Integer before = customerService.findAll().size();

        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setUsername("kaweel");
        createCustomerRequest.setPassword("1234");
        customerService.create(signInResponse.getToken(), createCustomerRequest);

        Integer after = customerService.findAll().size();

        Assertions.assertEquals(before + 1, after);

    }
}
