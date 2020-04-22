package com.example.api.exception;

/**
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 *
 */
public class LambdaFunctionException extends RuntimeException  {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public LambdaFunctionException(String message) {
		super(message);
	}
}