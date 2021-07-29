package br.gov.ans.snirabbitmq.core;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Envelope;

import br.gov.ans.snirabbitmq.config.SNIRabbitMQConnectionFactory;
import br.gov.ans.snirabbitmq.core.exceptions.SNIRabbitMQException;

@Component
public class SNNISimpleQueueConsumer implements QueueConsumer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8717697228597324845L;
	private static final Logger logger = LoggerFactory.getLogger(SNNISimpleQueueConsumer.class); // NOSONAR

	private transient SNIRabbitMQConnectionFactory connectionFactory;
	private transient AckConfirmConsumer consumer;
	private boolean started;
	private String queueName;
	private transient Thread thread;
	private int concurrentInstances;
	private AcknowledgeMode acknowledgeMode;
	private transient ChannelAndConnectionManager channelAndConnectionManager;

	public SNNISimpleQueueConsumer(SNNISimpleQueueConsumer clone) {
		Assert.notNull(clone, "It's not possible clone a null object");
		this.connectionFactory = clone.connectionFactory;
		this.consumer = clone.consumer;
		this.started = clone.started;
		this.queueName = clone.queueName;
		this.thread = clone.thread;
		this.concurrentInstances = clone.concurrentInstances;
		this.acknowledgeMode = clone.acknowledgeMode;
	}

	public SNNISimpleQueueConsumer(SNIRabbitMQConnectionFactory connectionFactory, AckConfirmConsumer consumer,String queueName, int concurrentInstances, AcknowledgeMode acknowledgeMode) {
		Assert.notNull(connectionFactory, "ConnectionFactory cannot be null");
		Assert.notNull(consumer, "Consumer cannot be null");
		Assert.notNull(queueName, "QueueName cannot be null");
		Assert.isTrue(concurrentInstances > 0, "Concurrent instances cannot be less than 1");
		this.concurrentInstances = concurrentInstances;
		this.connectionFactory = connectionFactory;
		this.consumer = consumer;
		this.queueName = queueName;
		this.acknowledgeMode = acknowledgeMode == null ? AcknowledgeMode.AUTO : AcknowledgeMode.MANUAL;
		this.channelAndConnectionManager = connectionFactory.getChannelAndConnectionManager();
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
	public AckConfirmConsumer getConsumer() {
		return consumer;
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
	}

	private String generatConsumerTag() {
		try {
			return this.channelAndConnectionManager.getChannel().basicConsume(this.queueName, null);
		} catch (IOException e) {
			throw new SNIRabbitMQException(e.getMessage(), e);
		}

	}

	private void consumeQueueLoop() {
//		String consumerTag = generatConsumerTag();
		try {
			logger.debug("reading queue " + this.getQueueName());
			
//			this.channelAndConnectionManager.getChannel().queueDeclare(queueName, true, false, false, null);
//			DeliverCallback deliverCallback = (consumerTagt, delivery) -> {
//			   String message = new String(delivery.getBody(), "UTF-8");
//			    System.out.println(" [x] Received '" + message + "'");
//			};
//			this.channelAndConnectionManager.getChannel().basicConsume(queueName, acknowledgeMode.isAutoAck(), this.consumer, consumerTagc->{});
			logger.debug("channel id"+this.channelAndConnectionManager.getChannel().toString());
			this.consumer.setChannel(this.channelAndConnectionManager.getChannel());
			this.channelAndConnectionManager.getChannel().basicConsume(queueName, false,"a-consumer-tag", this.consumer);
//			this.channelAndConnectionManager.getChannel().basicGet(queueName, acknowledgeMode.isAutoAck())
		} catch (IOException e) {
			throw new SNIRabbitMQException(e);
		}
	}
	private DefaultConsumer getDefault() {
		return new DefaultConsumer(this.channelAndConnectionManager.getChannel()) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)	throws IOException {
				try {
					logger.debug("channel id"+getChannel().toString());
					logger.debug(consumerTag);
					String status = new String(body);
					int i = (int) (Math.random()*1000);
					if (i % 5 == 0) { // mod 2
						logger.debug("rejected to dlx message: " + status);
						getChannel().basicReject(envelope.getDeliveryTag(), false);
					}
					if (i % 3 == 0) { // mod 3
						logger.debug("no ack message " + status);
						getChannel().basicNack(envelope.getDeliveryTag(), false, true);
					}

					logger.debug("ACK OK " + status); // resto
					getChannel().basicAck(envelope.getDeliveryTag(), false);

				} catch (IOException e) {
					throw new SNIRabbitMQException(e);
				}

			}
		};
	}

	public void setQueueName(String queueName) {
		Assert.notNull(queueName, "the queue name cannot be null");
		this.queueName = queueName;
	}

	@Override
	public boolean isThreadStarted() {
		if (thread == null) {
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
		this.channelAndConnectionManager = connectionFactory.getChannelAndConnectionManager();
	}

	@Override
	public void setConsumer(AckConfirmConsumer consumer) {
		this.consumer = consumer;

	}
}
