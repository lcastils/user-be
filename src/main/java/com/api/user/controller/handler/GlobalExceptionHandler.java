package com.api.user.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.api.user.dto.exception.ExceptionDTO;
import com.api.user.exception.BusinessException;

import lombok.extern.log4j.Log4j2;
@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ExceptionDTO> businessExceptionHandler(BusinessException e) {
        log.info("handler businessException");
        ExceptionDTO exceptionDTO = new ExceptionDTO( e.getMessage());
        return new ResponseEntity<>(exceptionDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ExceptionDTO> exceptionHandler(Exception e) {
        log.info("handler Exception");
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage());
        return new ResponseEntity<>(exceptionDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
   
}
