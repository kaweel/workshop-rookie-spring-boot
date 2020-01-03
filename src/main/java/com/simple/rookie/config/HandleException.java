package com.simple.rookie.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class HandleException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity exception(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity business(BusinessException e) {
        log.error(e.getResponseMsg().getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(e.getResponseMsg());
    }

}
