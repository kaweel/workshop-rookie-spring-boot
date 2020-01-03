package com.simple.rookie.dao.mapping;

import com.simple.rookie.enums.AddressType;

public interface CustomerAddressNative {

    void setCustomerId(Integer id);

    Integer getCustomerId();

    void setUserName(String userName);

    String getUserName();

    void setAddressId(Integer addressId);

    Integer getAddressId();

    void setType(AddressType type);

    AddressType getType();

    void setAddress(String address);

    String getAddress();

}
