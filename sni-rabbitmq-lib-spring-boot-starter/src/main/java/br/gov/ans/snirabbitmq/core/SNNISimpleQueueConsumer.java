package br.gov.ans.snirabbitmq.core;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.util.Assert;

import com.rabbitmq.client.Channel;

import br.gov.ans.snirabbitmq.config.SNIRabbitMQConnectionFactory;
import br.gov.ans.snirabbitmq.core.exceptions.SNIRabbitMQException;

public class SNNISimpleQueueConsumer implements QueueConsumer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8717697228597324845L;
	
	private transient SNIRabbitMQConnectionFactory connectionFactory;
	private MessageReader messageReader;
	private boolean started;
	private String queueName;
	
	private transient Thread thread;

	
	public SNNISimpleQueueConsumer(SNIRabbitMQConnectionFactory connectionFactory, MessageReader messageReader,	String queueName) {
		Assert.notNull(connectionFactory,"ConnectionFactory cannot be null");
		Assert.notNull(messageReader,"MessageReader cannot be null");
		Assert.notNull(queueName,"QueueName cannot be null");
		this.connectionFactory = connectionFactory;
		this.messageReader = messageReader;
		this.queueName = queueName;
	}

	@Override
	public void run() {
		this.consumeQueueLoop();

	}

	@Override
	public SNIRabbitMQConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	@Override
	public MessageReader getMessageReader() {
		return messageReader;
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public void startQueueConsumer() {
		this.thread = new Thread(this);
		this.started = true;
		this.thread.start();
	}

	@Override
	public void stopQueueConsumer() {
		this.started = false;

	}

	private Channel getChannel() throws IOException, TimeoutException {
		return this.getConnectionFactory().getConnection().createChannel();
	}

	@Override
	public void consumeQueueLoop() {
		while (this.isStarted()) {
			try {
				
				this.getMessageReader().readMessage(getChannel());

			} catch (IOException | TimeoutException e) {
				throw new SNIRabbitMQException(e.getMessage(), e);
			}
		}
	}
	public void setQueueName(String queueName) {
		Assert.notNull(queueName, "the queue name cannot be null");
		this.queueName = queueName;
	}


	@Override
	public boolean isThreadStarted() {
		return (thread ==null || !thread.isAlive());
	}

	@Override
	public String getQueueName() {
		return this.queueName;
	}

}
