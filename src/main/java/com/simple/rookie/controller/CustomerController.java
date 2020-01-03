package com.simple.rookie.controller;

import com.simple.rookie.controller.request.CreateCustomerRequest;
import com.simple.rookie.controller.request.UpdateCustomerRequest;
import com.simple.rookie.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity get(@RequestHeader("token") String token, @PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.get(token, id));
    }

    @PostMapping("")
    public ResponseEntity create(@RequestHeader("token") String token, @RequestBody CreateCustomerRequest request) {
        customerService.create(token, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("")
    public ResponseEntity update(@RequestHeader("token") String token, @RequestBody UpdateCustomerRequest request) {
        customerService.update(token, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@RequestHeader("token") String token, @PathVariable(name = "id") Integer id) {
        customerService.delete(token, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
