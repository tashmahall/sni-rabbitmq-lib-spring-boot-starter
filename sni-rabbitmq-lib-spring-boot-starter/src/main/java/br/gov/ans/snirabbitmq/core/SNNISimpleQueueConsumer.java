package br.gov.ans.snirabbitmq.core;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.rabbitmq.client.Channel;

import br.gov.ans.snirabbitmq.config.SNIRabbitMQConnectionFactory;
import br.gov.ans.snirabbitmq.core.exceptions.SNIRabbitMQException;
@Component
public class SNNISimpleQueueConsumer implements QueueConsumer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8717697228597324845L;
	private static final Logger logger=LoggerFactory.getLogger(SNNISimpleQueueConsumer.class); // NOSONAR
	
	private transient SNIRabbitMQConnectionFactory connectionFactory;
	private MessageReader messageReader;
	private boolean started;
	private String queueName;
	private transient Thread thread;
	private int concurrentInstances;
	private transient Channel channel;
	
	public SNNISimpleQueueConsumer(SNNISimpleQueueConsumer clone) {
		Assert.notNull(clone,"It's not possible clone a null object");
		this.connectionFactory=clone.connectionFactory;
		this.messageReader=clone.messageReader;
		this.started=clone.started;
		this.queueName=clone.queueName;
		this.thread=clone.thread;
		this.concurrentInstances=clone.concurrentInstances;
		this.channel=clone.channel;
	}

	
	public SNNISimpleQueueConsumer(SNIRabbitMQConnectionFactory connectionFactory, MessageReader messageReader,	String queueName,int concurrentInstances) {
		Assert.notNull(connectionFactory,"ConnectionFactory cannot be null");
		Assert.notNull(messageReader,"MessageReader cannot be null");
		Assert.notNull(queueName,"QueueName cannot be null");
		Assert.isTrue( concurrentInstances>0 ,"Concurrent instances cannot be less than 1");
		this.concurrentInstances= concurrentInstances;
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
		if(this.channel==null) {
			this.channel = this.getConnectionFactory().getConnection().createChannel();
		}
		return channel;
	}

	@Override
	public void consumeQueueLoop() {
		while (this.isStarted()) {
			try {
				logger.debug("reading queue "+ this.getQueueName());
				this.getMessageReader().readMessage(this.getChannel());

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
		if(thread ==null) {
			return false;
		}
		return thread.isAlive();
	}

	@Override
	public String getQueueName() {
		return this.queueName;
	}

	@Override
	public int getConcurrentInstaces() {
		
		return this.concurrentInstances;
	}

	public void setConnectionFactory(SNIRabbitMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

}
