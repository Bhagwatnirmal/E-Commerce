package com.E_Commerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.E_Commerce.dto.AuthenticationRequest;
import com.E_Commerce.dto.SignupRequest;
import com.E_Commerce.dto.UserDto;
import com.E_Commerce.repository.UserRepo;
import com.E_Commerce.services.auth.AuthService;
import com.E_Commerce.utils.JwtUtil;
import com.E_Commerce.Entity.User;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
public class AuthController 
{
	private final AuthenticationManager authenticationManager;
	
	private final UserDetailsService userDetailsService;
	
	private final UserRepo userRepo;
	
	private final JwtUtil jwtUtil;
	
	public static final String HEADER_STRING = "Authorization";
	
	public static final String TOKEN_PREFIX = "Bearer ";
	
	private final AuthService authService;
	
	@PostMapping("/authenticate")
	public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException, JSONException {
	    try {
	        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
	    } catch (BadCredentialsException e) {
	        throw new BadCredentialsException("Incorrect username or password.");
	    }

	    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
	    Optional<User> optionalUser = userRepo.findFirstByEmail(userDetails.getUsername());
	    final String jwt = jwtUtil.generateToken(userDetails.getUsername());

	    if (optionalUser.isPresent()) {
	        // Return user details as part of response body
	        JSONObject userResponse = new JSONObject()
	            .put("userId", optionalUser.get().getId())
	            .put("role", optionalUser.get().getRole());

	        // Set the Authorization header
	        response.addHeader("Authorization", "Bearer " + jwt);

	        // Expose the Authorization header so the client can read it
	        response.addHeader("Access-Control-Expose-Headers", "Authorization");

	        // CORS headers
	        response.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");

	        // Write user details as response body
	        response.getWriter().write(userResponse.toString());
	    }
	}

	
	@PostMapping("/sign-up")
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest)
	{
		if(authService.hasUserWithEmail(signupRequest.getEmail()))
		{
			return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
		}
		
		UserDto userDto = authService.createUser(signupRequest);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	@RequestMapping("/next")
	public String get(@RequestBody String userName)
	{
		return "index";
	}
}
