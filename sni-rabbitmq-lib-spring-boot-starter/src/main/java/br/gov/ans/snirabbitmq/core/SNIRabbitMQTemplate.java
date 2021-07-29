package br.gov.ans.snirabbitmq.core;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import br.gov.ans.snirabbitmq.config.SNIRabbitMQConnectionFactory;
import br.gov.ans.snirabbitmq.core.exceptions.SNIRabbitMQException;

public class SNIRabbitMQTemplate implements InitializingBean {
	/** Logger available to subclasses. */
	private static final Logger logger = LoggerFactory.getLogger(SNIRabbitMQTemplate.class); // NOSONAR

	private SNIRabbitMQConnectionFactory connectionFactory;

	private MessageConverter messageConverter;
	private Connection connection;
	private Channel channel;

	public SNIRabbitMQTemplate(SNIRabbitMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void convertAndSend(Object message, String exchangeName, String rountingKey, boolean mandatory) {
		logger.debug("Converting and sending a messege to exchange " + exchangeName + " and rounting Key " + rountingKey); //NOSONAR
		RabbitMQMessage rabbitMessage;
		try {
			this.openConnectionAndChannel();
			rabbitMessage = messageConverter.toMessage(message, null);
			channel.basicPublish(exchangeName, rountingKey, mandatory, false, rabbitMessage.getBasicProperties(),rabbitMessage.getBodyMessage());
		
			logger.debug("The message " + rabbitMessage.getBodyMessage().toString() + "has been sent"); //NOSONAR
			
			this.closeConnectionAndChannel();
		} catch (IOException e) {
			throw new SNIRabbitMQException(e.getMessage(), e);
		}

	}

	/**
	 * Set the ConnectionFactory to use for obtaining RabbitMQ {@link Connection
	 * Connections}.
	 *
	 * @param connectionFactory The connection factory.
	 */
	public void setConnectionFactory(SNIRabbitMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	/**
	 * @return The ConnectionFactory that this accessor uses for obtaining RabbitMQ
	 *         {@link Connection Connections}.
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

	private void openConnectionAndChannel() {
		try {
			if (connection == null || !connection.isOpen())
				this.connection = this.connectionFactory.getConnectionFactory().newConnection();
			if (channel == null || !channel.isOpen())
				this.channel = connection.createChannel();
		} catch (IOException | TimeoutException e) {
			throw new SNIRabbitMQException(e.getMessage(), e);
		}
	}

	private void closeConnectionAndChannel() {
		try {
			if (this.channel != null && this.channel.isOpen())
				this.channel.close();
			if (this.connection != null && this.connection.isOpen())
				this.connection.close();
		} catch (IOException | TimeoutException e) {
			throw new SNIRabbitMQException(e.getMessage(), e);
		}
	}

}
