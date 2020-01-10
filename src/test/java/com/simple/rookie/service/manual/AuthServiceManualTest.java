package com.simple.rookie.service.manual;

import com.simple.rookie.config.BusinessException;
import com.simple.rookie.controller.request.SignInRequest;
import com.simple.rookie.controller.response.SignInResponse;
import com.simple.rookie.dao.repository.CustomerRepository;
import com.simple.rookie.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthServiceManualTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void dummy() {
        AuthService service = new AuthService(new DummyCustomerRepository());
        Assertions.assertNotNull(service);
    }

    @Test
    void stub() {
        AuthService service = new AuthService(new StubCustomerRepository());
        SignInResponse signInResponse = service.signIn(new SignInRequest());
        Assertions.assertNotNull(signInResponse);
    }

    @Test
    void spy() {
        SpyCustomerRepository spy = new SpyCustomerRepository();
        AuthService service = new AuthService(spy);
        Assertions.assertThrows(BusinessException.class, () -> service.signIn(new SignInRequest()), "invalid credentials");
        Assertions.assertEquals(true, spy.isFindByUserNameWasCalled());
    }

    @Test
    void fake() {
        AuthService service = new AuthService(customerRepository);
        Assertions.assertThrows(BusinessException.class, () -> service.signIn(new SignInRequest()), "invalid credentials");
    }

    @Test
    void mock() {
        MockCustomerRepository mock = new MockCustomerRepository();
        AuthService service = new AuthService(mock);
        SignInResponse signInResponse = service.signIn(new SignInRequest());
        Assertions.assertNotNull(signInResponse);
        Assertions.assertEquals(true, mock.isFindByUserNameWasCalled());
    }
}
