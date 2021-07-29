package br.gov.ans.snirabbitmq.core;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import br.gov.ans.snirabbitmq.config.SNIRabbitMQConnectionFactory;
import br.gov.ans.snirabbitmq.core.exceptions.SNIRabbitMQException;

public class ChannelAndConnectionManager {
	private SNIRabbitMQConnectionFactory connectionFactory;
	private Channel channel;
	private Connection connection;
	public ChannelAndConnectionManager(SNIRabbitMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
		try {
			this.connection = connectionFactory.getConnectionFactory().newConnection();
			this.channel=connection.createChannel();
		} catch (IOException | TimeoutException e) {
			throw new SNIRabbitMQException(e);
		}
		
	}
	public boolean isConnectionOpen() {
		if(this.connection==null || !this.connection.isOpen()) {
			return false;
		}
		return true;
	}
	public boolean isChannelOpen() {
		if(this.channel==null || !this.channel.isOpen()) {
			return false;
		}
		return true;
	}
	public void openConnectionAndChannel() {
		try {
			if(connection == null || !connection.isOpen())
				this.connection = this.connectionFactory.getConnectionFactory().newConnection();
			if(channel == null || !channel.isOpen()) {
				this.channel = connection.createChannel();
			}
		} catch (IOException | TimeoutException e) {
			throw new SNIRabbitMQException(e.getMessage(), e);
		}
	}
	public void closeConnectionAndChannel()  {
		try {
			if(this.channel!=null && this.channel.isOpen())
				this.channel.close();
			if(this.connection !=null && this.connection.isOpen())
				this.connection.close();
		} catch (IOException | TimeoutException e) {
			throw new SNIRabbitMQException(e.getMessage(), e);
		}
	}
	public SNIRabbitMQConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}
	public Channel getChannel() {
		return channel;
	}
	public Connection getConnection() {
		return connection;
	}
	
}
