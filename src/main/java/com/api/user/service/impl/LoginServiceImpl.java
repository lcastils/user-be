package com.api.user.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.user.dto.UserRQ;
import com.api.user.dto.UserRS;
import com.api.user.exception.BusinessException;
import com.api.user.security.JwtUtilComponent;
import com.api.user.service.LoginService;
import com.api.user.service.UserService;
import com.api.user.utils.Converter;
import com.api.user.utils.ValidateUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserService userService;

	@Value("${dal.url}")
	private String dalUrl;

	@Value("${dal.login}")
	private String loginPath;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	JwtUtilComponent jwtUtil;

	@Override
	public UserRS login(UserRQ userRQ) {
		log.info("in login method");

		validateEmail(userRQ.getEmail());
		UserRS userRS = userService.getUserByEmail(userRQ.getEmail());
		if (Objects.nonNull(userRS)) {

			UserRQ userRQNew = Converter.getMapper().map(userRS, UserRQ.class);
			userRQNew.setToken(jwtUtil.getJWTToken(userRQ.getName()));
			userRQNew.setLastLogin(LocalDateTime.now());
			userRQNew.setPassword(userRQ.getPassword());

			UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(dalUrl).path(loginPath).build();
			HttpEntity<UserRQ> entity = new HttpEntity<>(userRQNew);
			ResponseEntity<UserRS> response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity,
					UserRS.class);
			if (HttpStatus.OK.equals(response.getStatusCode())) {
				return response.getBody();
			}
		}

		return null;
	}

	private void validateEmail(String email) {
		if (!ValidateUtil.isValidEmail(email)) {
			throw new BusinessException("Invalid email");
		}
	}

}
