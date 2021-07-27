package br.gov.ans.snirabbitmq.core;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import br.gov.ans.snirabbitmq.config.SNIRabbitMQConnectionFactory;

public interface QueueConsumer extends Runnable, Serializable{
	public SNIRabbitMQConnectionFactory getConnectionFactory();
	public void setConnectionFactory(SNIRabbitMQConnectionFactory connectionFactory);
	public MessageReader getMessageReader();
	public boolean isStarted();
	public void startQueueConsumer();
	public void stopQueueConsumer();
	public void consumeQueueLoop() throws IOException, TimeoutException;
	public boolean isThreadStarted();
	public String getQueueName();
	public int getConcurrentInstaces();
}
