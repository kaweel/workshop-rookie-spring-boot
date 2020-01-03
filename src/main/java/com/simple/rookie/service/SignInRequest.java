package com.simple.rookie.service;

import lombok.Data;

@Data
public class SignInRequest {
    private String userName;
    private String password;
}
