package br.gov.ans.snirabbitmq.core;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import br.gov.ans.snirabbitmq.core.exceptions.SNIRabbitMQException;

public class SNIRabbitMQTemplate implements InitializingBean{
	/** Logger available to subclasses. */
	protected final Log logger = LogFactory.getLog(getClass()); // NOSONAR
	
	private ConnectionFactory connectionFactory;
	
	public void send(RabbitMQMessage message, String exchangeName, String rountingKey, boolean mandatory) {
		
		try (Channel channel = connectionFactory.newConnection().createChannel()){
			channel.basicPublish(exchangeName, rountingKey, mandatory, false, message.getBasicProperties(), message.getBodyMessage());
		} catch (IOException | TimeoutException e) {
			throw new SNIRabbitMQException(e);
		}
	}	
	/**
	 * Set the ConnectionFactory to use for obtaining RabbitMQ {@link Connection Connections}.
	 *
	 * @param connectionFactory The connection factory.
	 */
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	/**
	 * @return The ConnectionFactory that this accessor uses for obtaining RabbitMQ {@link Connection Connections}.
	 */
	public ConnectionFactory getConnectionFactory() {
		return this.connectionFactory;
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.connectionFactory, "ConnectionFactory is required");
	}

	/**
	 * Create a RabbitMQ Connection via this template's ConnectionFactory and its host and port values.
	 * @return the new RabbitMQ Connection
	 * @see ConnectionFactory#createConnection
	 */
	protected Connection createConnection() {
		try {
			return this.connectionFactory.newConnection();
		} catch (IOException | TimeoutException e) {
			throw new SNIRabbitMQException(e);
		}
	}

}
