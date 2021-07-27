package br.gov.ans.snirabbitmq.core.exceptions;

public class SNIRabbitMQException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5647686230320613276L;

	public SNIRabbitMQException(String message) {
		super(message);
	}

	public SNIRabbitMQException(Throwable cause) {
		super(cause);
	}

	public SNIRabbitMQException(String message, Throwable cause) {
		super(message, cause);
	}

}
