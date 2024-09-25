package com.E_Commerce.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.E_Commerce.Entity.User;
import com.E_Commerce.dto.SignupRequest;
import com.E_Commerce.dto.UserDto;
import com.E_Commerce.enums.UserRole;
import com.E_Commerce.repository.UserRepo;

import jakarta.annotation.PostConstruct;

@Service
public class AuthServiceImpl implements AuthService 
{
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserDto createUser(SignupRequest signupRequest)
	{
		User user = new User();
		user.setEmail(signupRequest.getEmail());
		user.setName(signupRequest.getName());
		user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
		user.setRole(UserRole.CUSTOMER);
		User createdUser = userRepo.save(user);
		
		UserDto userDto = new UserDto();
		userDto.setId(createdUser.getId());
		
		return userDto;
	}
	
	public Boolean hasUserWithEmail(String email)
	{
		return userRepo.findFirstByEmail(email).isPresent();
	}
	
	@PostConstruct
	public void createAdminAccount() 
	{
		User adminAccount = userRepo.findByRole(UserRole.ADMIN);
		if(null == adminAccount)
		{
			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setName("admin");
			user.setRole(UserRole.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			
			userRepo.save(user);
		}
	}
}
