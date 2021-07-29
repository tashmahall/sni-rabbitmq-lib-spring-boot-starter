package br.gov.ans.snirabbitmq.core;

/**
 * Acknowledgment modes supported by the listener container.
 *
 */
public enum AcknowledgeMode {

	/**
	 * Manual acks - user must ack/nack via a channel aware listener.
	 */
	MANUAL(false),

	/**
	 * Auto - the container will issue the ack/nack based on whether
	 * the listener returns normally, or throws an exception.
	 * <p><em>Do not confuse with RabbitMQ {@code autoAck} which is
	 * represented by {@link #NONE} here</em>.
	 */
	AUTO(true);
	
	private boolean autoAck;
	private AcknowledgeMode (boolean autoAck){
	this.autoAck = autoAck;
}

	/**
	 * Return if transactions are allowed - if the mode is {@link #AUTO} or
	 * {@link #MANUAL}.
	 * @return true if transactions are allowed.
	 */
	public boolean isTransactionAllowed() {
		return this == AUTO || this == MANUAL;
	}

	/**
	 * Return if the mode is {@link #NONE} (which is called {@code autoAck}
	 * in RabbitMQ).
	 * @return true if the mode is {@link #NONE}.
	 */
	public boolean isAutoAck() {
		return this.autoAck;
	}


}