package com.api.user.service;

import java.util.List;

import com.api.user.dto.MessageDTO;
import com.api.user.dto.UserRQ;
import com.api.user.dto.UserRS;

public interface UserService {

    public UserRS createUser(final UserRQ user) ; 
    
    public UserRS getUserByEmail(final String email);

    public UserRS updateUser(UserRQ user);

    public MessageDTO deleteUser(UserRQ user);

	public List<UserRS> findAllUsers();

}
