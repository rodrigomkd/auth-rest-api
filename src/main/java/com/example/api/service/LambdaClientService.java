package com.example.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.example.api.exception.LambdaFunctionException;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

/**
 * @author <a href="mailto:rodrigo.moncada@ibm.com">Rodrigo Moncada</a>
 *
 */
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties
public class LambdaClientService {

	private final Logger classLogger = LoggerFactory.getLogger(this.getClass());
	
	private String lambdaFunction;
	private String accessKey;
	private String secretKey;
     
	/**
	 * Call AWS Lambda Function.
	 * 
	 * @param username
	 * @return String Lambda response 
	 */
    public String callFunction(String username) {
    	
    	System.setProperty("aws.accessKeyId", accessKey);
    	System.setProperty("aws.secretAccessKey", secretKey);
    	
        final LambdaClient lambdaClient = LambdaClient.builder()
        		.region(Region.of("us-east-1"))
        		.build();

        InvokeRequest req = InvokeRequest.builder().functionName(lambdaFunction)
                .payload(SdkBytes.fromUtf8String("{\n" + "\"username\" : \""+username+"\"\n" + "}"))
                .build();

        final InvokeResponse response = lambdaClient.invoke(req);

        if(response.statusCode() != 200) {
        	throw new LambdaFunctionException("Lambda Function Service is not available.");
        }
        
        classLogger.info("AWS Lambda response = " + response.payload().asUtf8String());
        
        return response.payload().asUtf8String();
    }
}
