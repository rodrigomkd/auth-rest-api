package com.example.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.dto.User;
import com.example.api.service.AuthService;

import io.swagger.annotations.ApiOperation;

/**
 * @author <a href="mailto:rodrigo.moncada@ibm.com">Rodrigo Moncada</a>
 *
 */
@RestController
@RequestMapping(AuthController.USER_API)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class AuthController {

	/**
	 * Logger instance
	 * 
	 * @see Logger
	 * @see LogManager
	 */
	private static final Logger logger = LogManager.getLogger(AuthController.class);
	
	public static final String USER_API = "/api/v1/auth";
	
	
	@Autowired
	private AuthService authService;
	
	/**
	 * Returns User authentication.
	 * 
	 * @param User - username and password
	 * @return ResponseEntity<Object> User object
	 */
	@ApiOperation(value = "Retrieve User Authentication", 
			notes = "Provide user and password")
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody User user) {
		logger.info("[POST] User Authentication.");
		
		//authentication with cognito
		authService.login(user.getUsername(), user.getPassword());
		
		logger.info("[POST] Authentication was success.");
		
		return ResponseEntity.ok(user);
	}
	
	@ApiOperation(value = "Retrieve welcome message", 
			notes = "Provide users welcome message")
	@GetMapping("/message")
	public ResponseEntity<Object> message(@RequestParam("username") String username) {
		logger.info("[GET] Retrieving user welcome message.");
		
		//call lambda function
		String message = authService.welcomeMessage(username);
		
		logger.info("[GET] Welcome message was success.");
		return ResponseEntity.ok(message);
	}
}
