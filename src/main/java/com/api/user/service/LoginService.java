package com.api.user.service;

import com.api.user.dto.UserRQ;
import com.api.user.dto.UserRS;

public interface LoginService {

	public UserRS login(UserRQ userRQ);

}
