package com.example.api.exception;

import java.util.Date;

/**
 * Custom class for Exception Response that will handle this application.<br>
 * <br>
 * 
 * Contains the basic information during the process of raise an Exception
 * 
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 *
 */
public class ExceptionResponse
{

	private Date timestamp;

	private String message;

	private String details;

	/**
	 * Constructor.
	 * 
	 * @param timestamp
	 * @param message
	 * @param details
	 */
	public ExceptionResponse(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

}
