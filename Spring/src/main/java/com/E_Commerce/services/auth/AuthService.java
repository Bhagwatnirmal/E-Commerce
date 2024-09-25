package com.E_Commerce.services.auth;

import com.E_Commerce.dto.SignupRequest;
import com.E_Commerce.dto.UserDto;

public interface AuthService 
{

	UserDto createUser(SignupRequest signupRequest);
	
	Boolean hasUserWithEmail(String email);
}
