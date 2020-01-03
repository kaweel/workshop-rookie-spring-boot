package com.simple.rookie.controller.request;

import com.simple.rookie.enums.AddressType;
import lombok.Data;

import java.util.List;

@Data
public class CreateCustomerRequest {
    private String username;
    private String password;
    private List<Address> address;

    @Data
    public static class Address {
        private String address;
        private AddressType type;
    }
}
