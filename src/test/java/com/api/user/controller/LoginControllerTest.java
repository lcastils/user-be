package com.api.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.api.user.dto.Phone;
import com.api.user.dto.UserRQ;
import com.api.user.dto.UserRS;
import com.api.user.service.LoginService;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

	private static final String TEST_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dGVrSldUIiwic3ViIjoiYmFyYmFyYSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE1OTk5NDc4ODEsImV4cCI6MTU5OTk0ODQ4MX0.aJtZ-QJyWjc9TgqHV1jPGoOWPtjKijMQ98EnOS8cy6GldiItgP-usobsA_YVBVdOGhJr--c0vU68Kg96wVxRlA";

	@InjectMocks
	private LoginController loginController;
	
	@Mock
	private LoginService loginService;

	private UserRQ objRq;

	private UserRS userRs;

	@Before
	public void init() {
		objRq = new UserRQ();
		objRq.setEmail("luiscastillo@domain.cl");
		objRq.setName("Luis");
		objRq.setPassword("apxbnA28");
		objRq.setToken(null);

		List<Phone> listPhones = new ArrayList<>();
		Phone objPhone = new Phone();
		objPhone.setCitycode(12L);
		objPhone.setCountrycode(2L);
		objPhone.setNumber(981271233L);
		listPhones.add(objPhone);
		objRq.setPhones(listPhones);

		userRs = new UserRS();
		userRs.setCreationDate(LocalDateTime.now());
		userRs.setEmail(objRq.getEmail());
		userRs.setId(1L);
		userRs.setToken(TEST_TOKEN);
		userRs.setIsActive(Boolean.TRUE);
		userRs.setName(objRq.getName());
		userRs.setPhones(listPhones);
		
		Mockito.when(loginService.login(Mockito.any())).thenReturn(userRs);
	}

	@Test
	public void loginOk() {
		ResponseEntity<UserRS> response = loginController.login(objRq);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void loginFail() {
		Mockito.when(loginService.login(Mockito.any())).thenReturn(null);
		ResponseEntity<UserRS> response = loginController.login(objRq);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}
