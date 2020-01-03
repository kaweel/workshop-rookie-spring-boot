package com.simple.rookie.controller.request;

import com.simple.rookie.enums.AddressType;
import lombok.Data;

@Data
public class UpdateAddressRequest {
    private Integer id;
    private String address;
    private AddressType type;
}
