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

import br.gov.ans.snirabbitmq.config.SNIRabbitMQConnectionFactory;
import br.gov.ans.snirabbitmq.core.exceptions.SNIRabbitMQException;

public class SNIRabbitMQTemplate implements InitializingBean{
	/** Logger available to subclasses. */
	protected final Log logger = LogFactory.getLog(getClass()); // NOSONAR
	
	private SNIRabbitMQConnectionFactory connectionFactory;
	
	private MessageConverter messageConverter;
	
	
	public SNIRabbitMQTemplate(SNIRabbitMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	public void convertAndSend(Object message, String exchangeName, String rountingKey, boolean mandatory) {
		logger.debug("Converting and sending a messege to exchange "+ exchangeName+ " and rounting Key "+ rountingKey);
		try (Channel channel = connectionFactory.getConnection().createChannel()){
			RabbitMQMessage rabbitMessage = messageConverter.toMessage(message, null);
			channel.basicPublish(exchangeName, rountingKey, mandatory, false, rabbitMessage.getBasicProperties(), rabbitMessage.getBodyMessage());
			logger.debug("The message "+ rabbitMessage.getBodyMessage().toString() + "has been sent");
		} catch (IOException | TimeoutException e) {
			throw new SNIRabbitMQException(e);
		}
	}	
	/**
	 * Set the ConnectionFactory to use for obtaining RabbitMQ {@link Connection Connections}.
	 *
	 * @param connectionFactory The connection factory.
	 */
	public void setConnectionFactory(SNIRabbitMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	/**
	 * @return The ConnectionFactory that this accessor uses for obtaining RabbitMQ {@link Connection Connections}.
	 */
	public ConnectionFactory getConnectionFactory() {
		return this.connectionFactory.getConnectionFactory();
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.connectionFactory, "ConnectionFactory is required");
	}

	public MessageConverter getMessageConverter() {
		return messageConverter;
	}
	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}
	/**
	 * Create a RabbitMQ Connection via this template's ConnectionFactory and its host and port values.
	 * @return the new RabbitMQ Connection
	 * @see ConnectionFactory#createConnection
	 */
	protected Connection createConnection() {
		try {
			return this.connectionFactory.getConnection();
		} catch (IOException | TimeoutException e) {
			throw new SNIRabbitMQException(e);
		}
	}

}
