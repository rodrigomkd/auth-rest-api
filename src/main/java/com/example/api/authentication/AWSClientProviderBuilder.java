package com.example.api.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.api.util.AWSConfig;

/**
 * AWSClientProviderBuilder.java class that contains the logic 
 * @author Rodrigo Moncada
 * @version 
 * @since 2020-04-19 
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AWSClientProviderBuilder {

	@Autowired
	private  AWSConfig cognitoConfig;

	private AWSCognitoIdentityProvider cognitoIdentityProvider;
	private ClasspathPropertiesFileCredentialsProvider propertiesFileCredentialsProvider;
	private String region;
	
	/**
	 * getAWSCognitoIdentityClient 
	 *@return void
	 */
	private void initCommonInfo() {
		if(null == propertiesFileCredentialsProvider) {
			propertiesFileCredentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
		}
		if(null == region) {
			region = cognitoConfig.getRegion();
		}
	}

	public AWSCognitoIdentityProvider getAWSCognitoIdentityClient() {
		if( null == cognitoIdentityProvider) {
			initCommonInfo();

			cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
					.withCredentials(propertiesFileCredentialsProvider)
					.withRegion(region)
					.build();
		}

		return cognitoIdentityProvider;
	}
}