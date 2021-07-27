package br.gov.ans.snirabbitmq.core.exceptions;

public class MessageConversionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5647686230320613276L;

	public MessageConversionException(String message) {
		super(message);
	}

	public MessageConversionException(Throwable cause) {
		super(cause);
	}

	public MessageConversionException(String message, Throwable cause) {
		super(message, cause);
	}

}
