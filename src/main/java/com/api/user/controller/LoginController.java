package com.api.user.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.user.dto.UserRQ;
import com.api.user.dto.UserRS;
import com.api.user.exception.BusinessException;
import com.api.user.security.JwtUtil;
import com.api.user.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    @ApiOperation(value = "Metodo encargado de realizar login ")
    public ResponseEntity<UserRS> login(@RequestBody UserRQ userRQ) {
        UserRS response = userService.getUserByEmail(userRQ.getEmail());
        if (Objects.nonNull(response)) {
            response.setName(response.getName());
            response.setToken(jwtUtil.getJWTToken(response.getName()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        throw new BusinessException("USER NOT FOUND");

    }

}
