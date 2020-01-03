package com.simple.rookie.controller.request;

import com.simple.rookie.enums.AddressType;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCustomerRequest {
    private Integer id;
    private String username;
    private List<Address> address;

    @Data
    public static class Address {
        private Integer id;
        private String address;
        private AddressType type;
    }
}
