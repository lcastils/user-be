package com.api.user.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.user.dto.MessageDTO;
import com.api.user.dto.UserRQ;
import com.api.user.dto.UserRS;
import com.api.user.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/users/findall", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Metodo encargado de devolver los usuarios registrados activos ")
	public ResponseEntity<List<UserRS>> findAllUsers() {
		log.info("find all  user ");
		List<UserRS> objResponse = userService.findAllUsers();
		if (!objResponse.isEmpty()) {
			return new ResponseEntity<>(objResponse, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping(value = "/users/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Metodo encargado de crear un usuario ")
	public ResponseEntity<UserRS> createuser(@RequestBody UserRQ user) {
		log.info("create user ");
		UserRS objResponse = userService.createUser(user);
		if (Objects.nonNull(objResponse)) {
			return new ResponseEntity<>(objResponse, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping(value = "/users/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Metodo encargado de actualizar un usuario ")
	public ResponseEntity<UserRS> updateUser(@RequestBody UserRQ user) {
		log.info("update user");

		UserRS objResponse = userService.updateUser(user);
		if (Objects.nonNull(objResponse)) {
			return new ResponseEntity<>(objResponse, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping(value = "/users/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Metodo encargado de  eliminar un usuario")
	public ResponseEntity<MessageDTO> deleteUser(@RequestBody UserRQ user) {
		log.info("delete user");

		MessageDTO objResponse = userService.deleteUser(user);

		if (Objects.nonNull(objResponse)) {
			return new ResponseEntity<>(objResponse, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
