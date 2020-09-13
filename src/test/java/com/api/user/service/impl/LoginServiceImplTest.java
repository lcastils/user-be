package com.api.user.service.impl;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.api.user.dto.Phone;
import com.api.user.dto.UserRQ;
import com.api.user.dto.UserRS;
import com.api.user.exception.BusinessException;
import com.api.user.security.JwtUtilComponent;
import com.api.user.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceImplTest {

	private static final String TEST_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dGVrSldUIiwic3ViIjoiYmFyYmFyYSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE1OTk5NDc4ODEsImV4cCI6MTU5OTk0ODQ4MX0.aJtZ-QJyWjc9TgqHV1jPGoOWPtjKijMQ98EnOS8cy6GldiItgP-usobsA_YVBVdOGhJr--c0vU68Kg96wVxRlA";

	@InjectMocks
	private LoginServiceImpl loginService;
	
	@Mock
	private UserService userService;

	private UserRQ objRq;

	private UserRS userRs;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private JwtUtilComponent jwtutil;

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

		ReflectionTestUtils.setField(loginService, "dalUrl", "http://localhost:8081/");
		ReflectionTestUtils.setField(loginService, "loginPath", "/test/");
		
		Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(userRs);
		
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.POST),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(userRs, HttpStatus.OK));
	}

	@Test(expected = BusinessException.class)
	public void loginUserInvalidEmail() {
		objRq.setEmail("luasda.asa@ascas@.com");
		loginService.login(objRq);
	}
	
	@Test 
	public void loginUserOk() {
		assertNotNull("login ok ", loginService.login(objRq));;
	}
	
	@Test(expected = BusinessException.class)
	public void loginUserNotFound() {
		Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(null);
		 loginService.login(objRq);
	}

 


}
