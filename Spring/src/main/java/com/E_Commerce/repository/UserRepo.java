package com.E_Commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.E_Commerce.Entity.User;
import com.E_Commerce.enums.UserRole;

@Repository
public interface UserRepo extends JpaRepository<User, Long> 
{
	Optional<User> findFirstByEmail(String email);
	User findByRole(UserRole userRole);
}
