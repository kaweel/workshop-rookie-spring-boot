package com.simple.rookie.controller;

import com.simple.rookie.controller.request.SignInRequest;
import com.simple.rookie.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.signIn(request));
    }
}
