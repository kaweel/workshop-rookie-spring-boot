package com.simple.rookie.enums;

import lombok.Getter;

@Getter
public enum AddressType {

    OFFICE("OFFICE"),
    HOME("HOME");

    private String value;

    AddressType(String value) {
        this.value = value;
    }

}
