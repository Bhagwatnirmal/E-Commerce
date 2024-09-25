package com.E_Commerce.dto;

import com.E_Commerce.enums.UserRole;

import lombok.Data;

@Data
public class UserDto
{
	
	private Long id;
	
	private String email;
	
	private String name;
	
	private UserRole userRole;
}
