package com.simple.rookie.controller.request;

import lombok.Data;

@Data
public class SignInRequest {
    private String username;
    private String password;
}
