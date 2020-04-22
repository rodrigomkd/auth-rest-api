package com.example.api.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.dto.User;


/**
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 *
 */
@Service
public class AuthService {
	
	/**
	 * Logger instance
	 * 
	 * @see Logger
	 * @see LogManager
	 */
	private static final Logger logger = LogManager.getLogger(AuthService.class);
	
	@Autowired
	private CognitoAuthenticationService cognitoService;
	
	@Autowired
	private LambdaClientService lambdaClientService;
	
	/**
	 * Returns a valid Token.
	 * 
	 * @param username
	 * @param password
	 * @return User 
	 */
	public User login(String username, String password) {
		logger.info("Entering login()");
		User user = cognitoService.authenticate(username, password);
		logger.info("Exiting login()");
		
		return user;
	}
	
	/**
	 * Get message from Lambda Function.
	 * 
	 * @param username
	 * @return String message
	 */
	public String welcomeMessage(String username) {
		logger.info("Entering welcomeMessage()");
		String message = lambdaClientService.callFunction(username);
		logger.info("Exiting welcomeMessage()");
		
		return message;
	}
}