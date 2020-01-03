package com.simple.rookie.dao.mapping;

import com.simple.rookie.enums.AddressType;
import lombok.Data;

@Data
public class CustomerAddressJQL {
    private Integer customerId;
    private String userName;
    private Integer addressId;
    private AddressType addressType;
    private String address;

    public CustomerAddressJQL(Integer customerId, String userName, Integer addressId, AddressType addressType, String address) {
        this.customerId = customerId;
        this.userName = userName;
        this.addressId = addressId;
        this.addressType = addressType;
        this.address = address;
    }
}

