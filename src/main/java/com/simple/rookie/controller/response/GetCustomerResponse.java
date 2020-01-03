package com.simple.rookie.controller.response;

import com.simple.rookie.enums.AddressType;
import lombok.Data;

import java.util.List;

@Data
public class GetCustomerResponse {
    private Integer id;
    private String username;
    private List<Address> address;

    @Data
    public static class Address {
        private Integer id;
        private AddressType type;
        private String address;
    }
}
