package com.example.api.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.example.api.authentication.AWSClientProviderBuilder;
import com.example.api.dto.User;
import com.example.api.exception.CognitoException;
import com.example.api.util.AWSConfig;

/**
 * CognitoAuthenticationService.java class that contains the logic to connect to Cognito using Username and password.
 * @author Rodrigo Moncada
 * @version 1.0 
 * @since 2020-04-19 
 */
@Service
public class CognitoAuthenticationService {

	/**New password key*/
	private static final String NEW_PASS_WORD = "NEW_PASSWORD";
	/**New password required challenge key*/
	private static final String NEW_PASS_WORD_REQUIRED = "NEW_PASSWORD_REQUIRED";
	/**Password key*/
	private static final String PASS_WORD = "PASSWORD";
	/**Username key*/
	private static final String USERNAME = "USERNAME";

	private final Logger classLogger = LoggerFactory.getLogger(this.getClass());


	@Autowired 
	AWSClientProviderBuilder cognitoBuilder;

	@Autowired
	private AWSConfig cognitoConfig;

	/**
	 * getAmazonCognitoIdentityClient 
	 *@return AWSCognitoIdentityProvider
	 */
	private AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
		return cognitoBuilder.getAWSCognitoIdentityClient();
	}

	

	/**
	 * Method that contains the logic of authentication with AWS Cognito.
	 *@param authenticationRequest
	 *@return User 
	 */
	public User authenticate(String username, String password) {

		//AuthenticationResultType authenticationResult = null;
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();

		try {

			final Map<String, String> authParams = new HashMap<>();
			authParams.put(USERNAME, username);
			authParams.put(PASS_WORD, password);

			final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();
			authRequest.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
			.withClientId(cognitoConfig.getClientId())			
			.withUserPoolId(cognitoConfig.getPoolId())
			.withAuthParameters(authParams);

			AdminInitiateAuthResult result = cognitoClient.adminInitiateAuth(authRequest);


			//Has a Challenge
			if(result.getChallengeName() != null && result.getChallengeName().isEmpty()) {

				//If the challenge is required new Password validates if it has the new password variable.
				if(NEW_PASS_WORD_REQUIRED.equals(result.getChallengeName())){
					//we still need the username

					final Map<String, String> challengeResponses = new HashMap<>();
					challengeResponses.put(USERNAME, username);
					challengeResponses.put(PASS_WORD, password);

					//add the new password to the params map
					challengeResponses.put(NEW_PASS_WORD, password);

					//populate the challenge response
					final AdminRespondToAuthChallengeRequest request = new AdminRespondToAuthChallengeRequest();
					request.withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
					.withChallengeResponses(challengeResponses)
					.withClientId(cognitoConfig.getClientId())
					.withUserPoolId(cognitoConfig.getPoolId())
					.withSession(result.getSession());

					AdminRespondToAuthChallengeResult resultChallenge = cognitoClient.adminRespondToAuthChallenge(request);
					
					classLogger.info("AWS Cognito response: " + resultChallenge.getAuthenticationResult());
				}else {
					//has another challenge
					throw new CognitoException(result.getChallengeName(), CognitoException.USER_MUST_DO_ANOTHER_CHALLENGE, result.getChallengeName());
				}
			}

			if(classLogger.isInfoEnabled()) {
				classLogger.info("User successfully authenticated userInfo: username {}", username);
			}

			return new User(username, password);
		}catch(com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e) {
			classLogger.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(),e.getErrorCode(), e.getMessage() + e.getErrorCode());
		}catch (CognitoException cognitoException) {
			throw cognitoException;
		}catch(Exception e) {
			classLogger.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE,e.getMessage());
		}

	}

}