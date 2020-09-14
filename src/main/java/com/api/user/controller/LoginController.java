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
import com.api.user.service.LoginService;

import io.swagger.annotations.ApiOperation;

@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;

	@PostMapping("/login")
	@ApiOperation(value = "Metodo encargado de realizar login ")
	public ResponseEntity<UserRS> login(@RequestBody UserRQ userRQ) {
		UserRS response = loginService.login(userRQ);
		if (Objects.nonNull(response)) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

}
