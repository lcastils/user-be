package com.api.user.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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

import com.api.user.dto.MessageDTO;
import com.api.user.dto.Phone;
import com.api.user.dto.UserRQ;
import com.api.user.dto.UserRS;
import com.api.user.exception.BusinessException;
import com.api.user.security.JwtUtilComponent;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

	private static final String TEST_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dGVrSldUIiwic3ViIjoiYmFyYmFyYSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE1OTk5NDc4ODEsImV4cCI6MTU5OTk0ODQ4MX0.aJtZ-QJyWjc9TgqHV1jPGoOWPtjKijMQ98EnOS8cy6GldiItgP-usobsA_YVBVdOGhJr--c0vU68Kg96wVxRlA";

	@InjectMocks
	private UserServiceImpl userService;

	private UserRQ objRq;

	private UserRS userRs;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private JwtUtilComponent jwtutil;

	@Before
	public void init() {

		ReflectionTestUtils.setField(userService, "dalUrl", "http://localhost:8081/");
		ReflectionTestUtils.setField(userService, "findByEmail", "/test/");
		ReflectionTestUtils.setField(userService, "dalUserCreate", "/user/");
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
		Mockito.when(jwtutil.getJWTToken(Mockito.anyString())).thenReturn(TEST_TOKEN);
	}

	@Test(expected = BusinessException.class)
	public void createUserInvalidEmail() {
		objRq.setEmail("luasda.asa@ascas@.com");
		userService.createUser(objRq);
	}

	@Test
	public void createUserOK() {
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.GET),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.POST),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(userRs, HttpStatus.OK));
		UserRS userCreated = userService.createUser(objRq);
		assertNotNull("user created : ", userCreated);
	}

	@Test(expected = BusinessException.class)
	public void createUserWithEmailUsed() {
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.GET),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(userRs, HttpStatus.OK));
		userService.createUser(objRq);
	}

	@Test
	public void createUserFail() {
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.GET),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.POST),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(HttpStatus.BAD_GATEWAY));
		UserRS userCreated = userService.createUser(objRq);
		assertNull("something went grong : ", userCreated);
	}

	@Test(expected = BusinessException.class)
	public void updateUserFailUserNotFound() {
		objRq.setEmail("pepito123@ribera.cl");
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.GET),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
		userService.updateUser(objRq);
	}

	@Test
	public void updateUserOK() {
		userRs.setLastLogin(LocalDateTime.now());
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.GET),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(userRs, HttpStatus.OK));
		userRs.getPhones().stream().filter(filterCityCode()).forEach(p -> p.setCitycode(13L));
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.PUT),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(userRs, HttpStatus.OK));
		UserRS userUpdated = userService.updateUser(objRq);
		assertNotNull("user updated ok  : ", userUpdated);
	}

	private Predicate<? super Phone> filterCityCode() {
		return x -> x.getCitycode().equals(12L);
	}
	
	@Test
	public void updateUserFail() {
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.GET),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(userRs, HttpStatus.OK));
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.PUT),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(HttpStatus.BAD_GATEWAY));
		UserRS userUpdated = userService.updateUser(objRq);
		assertNull("something went grong : ", userUpdated);
	}
	
	@Test(expected = BusinessException.class)
	public void deleteUserFailUserNotFound() {
		objRq.setEmail("juanZapata123@ribera.cl");
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.GET),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
		userService.deleteUser(objRq);
	}
	
	@Test
	public void deleteUserOK() {
		userRs.setLastLogin(LocalDateTime.now());
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.GET),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(userRs, HttpStatus.OK));
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.DELETE),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(userRs, HttpStatus.OK));
		MessageDTO messageDelete = userService.deleteUser(objRq);
		assertNotNull("user delete ok  : ", messageDelete);
	}
	
	@Test
	public void deleteUserFail() {
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.GET),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(userRs, HttpStatus.OK));
		Mockito.when(restTemplate.exchange(ArgumentMatchers.any(), ArgumentMatchers.eq(HttpMethod.DELETE),
				ArgumentMatchers.any(), ArgumentMatchers.eq(UserRS.class)))
				.thenReturn(new ResponseEntity<>(HttpStatus.BAD_GATEWAY));
		MessageDTO messageDelete = userService.deleteUser(objRq);
		assertNull("something went grong : ", messageDelete);
	}

}
