package com.E_Commerce.services.jwt;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.E_Commerce.Entity.User;

import com.E_Commerce.repository.UserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService 
{

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		Optional<User> optionalUser = userRepo.findFirstByEmail(username);
		if(optionalUser.isEmpty()) throw new UsernameNotFoundException("Username not found", null);
		return new org.springframework.security.core.userdetails.User(optionalUser.get().getEmail(), optionalUser.get().getPassword(), new ArrayList<>());
	}

}
