package com.simple.rookie.service;

import com.simple.rookie.config.BusinessException;
import com.simple.rookie.controller.request.SignInRequest;
import com.simple.rookie.controller.response.SignInResponse;
import com.simple.rookie.dao.entity.Customer;
import com.simple.rookie.dao.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    CustomerRepository customerRepository;

    @Test
    @DisplayName("signin fail with invalid_user")
    public void invalid_user() {
        BDDMockito.given(customerRepository.auth(Mockito.any(), Mockito.any())).willReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, () -> authService.signIn(new SignInRequest()), "invalid credentials");
    }

    @Test
    @DisplayName("signin success")
    public void success() {
        BDDMockito.given(customerRepository.auth(Mockito.any(), Mockito.any())).willReturn(Optional.of(new Customer()));
        SignInResponse actual = authService.signIn(new SignInRequest());
        Assertions.assertNotNull(actual);
    }
}
