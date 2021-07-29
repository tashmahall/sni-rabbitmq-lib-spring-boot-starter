package br.gov.ans.snirabbitmq.core;

import java.io.Serializable;

import com.rabbitmq.client.DefaultConsumer;

import br.gov.ans.snirabbitmq.config.SNIRabbitMQConnectionFactory;

public interface QueueConsumer extends Runnable, Serializable{
	public SNIRabbitMQConnectionFactory getConnectionFactory();
	public void setConnectionFactory(SNIRabbitMQConnectionFactory connectionFactory);
	public AckConfirmConsumer getConsumer();
	public void setConsumer(AckConfirmConsumer consumer);
	public boolean isStarted();
	public void startQueueConsumer();
	public void stopQueueConsumer();
	public boolean isThreadStarted();
	public String getQueueName();
	public int getConcurrentInstaces();
}
