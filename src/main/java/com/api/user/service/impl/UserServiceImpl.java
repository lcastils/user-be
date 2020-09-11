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

import com.api.user.dto.MessageDTO;
import com.api.user.dto.UserRQ;
import com.api.user.dto.UserRS;
import com.api.user.exception.BusinessException;
import com.api.user.security.JwtUtil;
import com.api.user.service.UserService;
import com.api.user.utils.ValidateUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dal.url}")
    private String dalUrl;

    @Value("${dal.user.find-by.email}")
    private String findByEmail;

    @Value("${dal.user.create}")
    private String dalUserCreate;
    
    @Autowired
    private JwtUtil jwtutil;

    @Override
    public UserRS createUser(UserRQ user) {

        validateEmail(user.getEmail());
        UserRS userRS = getUserByEmail(user.getEmail());
        if (Objects.nonNull(userRS)) {
            throw new BusinessException("Email esta en uso");
        }

        validatePassWord(user.getPassword());
       

        user.setToken(jwtutil.getJWTToken(user.getName()));
        user.setCreationDate(LocalDateTime.now());
        user.setIsActive(Boolean.TRUE);
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(dalUrl).path(dalUserCreate).build();
        HttpEntity<UserRQ> entity = new HttpEntity<>(user);
        ResponseEntity<UserRS> response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity,
                UserRS.class);
        if(HttpStatus.OK.equals(response.getStatusCode())){
            return response.getBody();
        }
        
        return null;

    }

    private void validatePassWord(String password) {
         log.info("pendiente validacion password"+ password);
        //TODO validate password 
    }

    private void validateEmail(String  email) {
        if (!ValidateUtil.isValidEmail(email)) {
            throw new BusinessException("Invalid email");
        }
    }

    @Override
    public UserRS getUserByEmail(final String email) {
        validateEmail(email);
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(dalUrl).path(findByEmail).buildAndExpand(email);
        ResponseEntity<UserRS> response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null,
                UserRS.class);
        if (HttpStatus.OK.equals(response.getStatusCode())) {
            UserRS userRS = response.getBody();
            if(Objects.isNull(userRS.getLastLogin())) {
                userRS.setLastLogin(userRS.getCreationDate());
            }
            return userRS;
        }

        return null;
    }

    @Override
    public UserRS updateUser(UserRQ user) {
        UserRS userRS = getUserByEmail(user.getEmail());
        if(Objects.isNull(userRS)) {
            throw new BusinessException("Usuario no registrado");
        }
        LocalDateTime dateNow = LocalDateTime.now();
        validatePassWord(user.getPassword());
        
        user.setId(userRS.getId());
        user.setCreationDate(userRS.getCreationDate());
        user.setToken(jwtutil.getJWTToken(user.getName()));
        user.setIsActive(Boolean.TRUE);
        user.setUpdateDate(dateNow);
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(dalUrl).path(dalUserCreate).build();
        HttpEntity<UserRQ> entity = new HttpEntity<>(user);
        ResponseEntity<UserRS> response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, entity,
                UserRS.class);
        if(HttpStatus.OK.equals(response.getStatusCode())){
            return response.getBody();
        }
        
        return null;
    }

    @Override
    public MessageDTO deleteUser(UserRQ user) {
        UserRS userRS = getUserByEmail(user.getEmail());
        if(Objects.isNull(userRS)) {
            throw new BusinessException("Usuario no registrado");
        }
        LocalDateTime dateNow = LocalDateTime.now();
        validatePassWord(user.getPassword());
        
        user.setId(userRS.getId());
        user.setCreationDate(userRS.getCreationDate());
        user.setIsActive(Boolean.FALSE);
        user.setUpdateDate(dateNow);
        user.setToken(userRS.getToken());
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(dalUrl).path(dalUserCreate).build();
        HttpEntity<UserRQ> entity = new HttpEntity<>(user);
        ResponseEntity<UserRS> response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.DELETE, entity,
                UserRS.class);
        if(HttpStatus.OK.equals(response.getStatusCode())){
            return new MessageDTO("Usuario eliminado exitosamente");
        }
        
        return null;
    }

}
