package com.simple.rookie.service;

import com.simple.rookie.controller.request.CreateCustomerRequest;
import com.simple.rookie.controller.request.SignInRequest;
import com.simple.rookie.controller.response.SignInResponse;
import com.simple.rookie.dao.repository.CustomerRepository;
import com.simple.rookie.enums.AddressType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "it")
public class CustomerServiceIT {

    public static final String USER_TEST = "test";

    @Autowired
    AuthService authService;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    @Transactional
    public void create_success() {

        customerRepository.deleteByUserName(USER_TEST);

        SignInRequest request = new SignInRequest();
        request.setUsername("ink");
        request.setPassword("1234");
        SignInResponse signInResponse = authService.signIn(request);

        Integer before = customerService.findAll().size();

        CreateCustomerRequest.Address address = new CreateCustomerRequest.Address();
        address.setType(AddressType.HOME);
        address.setAddress("test test test");

        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setUsername(USER_TEST);
        createCustomerRequest.setPassword("1234");
        createCustomerRequest.setAddress(Arrays.asList(address));
        customerService.create(signInResponse.getToken(), createCustomerRequest);

        Integer after = customerService.findAll().size();

        Assertions.assertEquals(before + 1, after);

    }
}
