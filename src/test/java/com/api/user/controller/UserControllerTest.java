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

import com.api.user.dto.MessageDTO;
import com.api.user.dto.Phone;
import com.api.user.dto.UserRQ;
import com.api.user.dto.UserRS;
import com.api.user.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
	
	private static final String TEST_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dGVrSldUIiwic3ViIjoiYmFyYmFyYSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE1OTk5NDc4ODEsImV4cCI6MTU5OTk0ODQ4MX0.aJtZ-QJyWjc9TgqHV1jPGoOWPtjKijMQ98EnOS8cy6GldiItgP-usobsA_YVBVdOGhJr--c0vU68Kg96wVxRlA";

	@InjectMocks
	private UserController userController;
	
	@Mock
	private UserService userService;

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
		MessageDTO message = new MessageDTO();
		message.setMessage("user delete success");
		List<UserRS> listResponse = new ArrayList<>();
		listResponse.add(userRs);
		Mockito.when(userService.findAllUsers()).thenReturn(listResponse);
		Mockito.when(userService.createUser(Mockito.any())).thenReturn(userRs);
		Mockito.when(userService.updateUser(Mockito.any())).thenReturn(userRs);
		Mockito.when(userService.deleteUser(Mockito.any())).thenReturn(message);
		Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(userRs);
	}
	
	@Test
	public void createUserOk() {
		ResponseEntity<UserRS> response = userController.createuser(objRq);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void createUserFail() {
		Mockito.when(userService.createUser(Mockito.any())).thenReturn(null);
		ResponseEntity<UserRS> response = userController.createuser(objRq);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
	
	@Test
	public void updateUserOk() {
		ResponseEntity<UserRS> response = userController.updateUser(objRq);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void udpateUserFail() {
		Mockito.when(userService.updateUser(Mockito.any())).thenReturn(null);
		ResponseEntity<UserRS> response = userController.updateUser(objRq);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void deleteUserOk() {
		ResponseEntity<MessageDTO> response = userController.deleteUser(objRq);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void deleteUserFail() {
		Mockito.when(userService.deleteUser(Mockito.any())).thenReturn(null);
		ResponseEntity<MessageDTO> response = userController.deleteUser(objRq);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void findAllUsersOk() {
		ResponseEntity<List<UserRS>> response = userController.findAllUsers();
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void findAllUsersEmpty() {
		Mockito.when(userService.findAllUsers()).thenReturn(new ArrayList<>());
		ResponseEntity<List<UserRS>> response = userController.findAllUsers();
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
 

}
