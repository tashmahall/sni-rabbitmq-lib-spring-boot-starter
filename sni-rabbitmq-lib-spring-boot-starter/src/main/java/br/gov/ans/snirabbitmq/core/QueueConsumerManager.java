package br.gov.ans.snirabbitmq.core;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
@Component
public class QueueConsumerManager implements InitializingBean {
	private static final Logger logger=LoggerFactory.getLogger(QueueConsumerManager.class); // NOSONAR
	private final Map<String, QueueConsumerEncapsulator> queueConsumers = new TreeMap<>();
	@Override
	public void afterPropertiesSet() throws Exception {
		startAllQueueConsumers();
	}

	public void addQueueConsumer(QueueConsumer queueConsumer) {
		Assert.isTrue((queueConsumer.getConcurrentInstaces() > 0), "the instances number must be bigger than one");
		logger.debug("Adding "+queueConsumer.getConcurrentInstaces()+ " of a QueueConsumer with queue name "+ queueConsumer.getQueueName());
		QueueConsumerEncapsulator qce = new QueueConsumerEncapsulator(queueConsumer, queueConsumer.getConcurrentInstaces());
		stopSpecificQueueConsumers(queueConsumer.getQueueName());
		queueConsumers.put(qce.getQueueConsumer().getQueueName(), qce);
		startSpecificQueueConsumers(queueConsumer.getQueueName());
		logger.debug("The instances of Queue Consumer have started");
	}

	private void startSpecificQueueConsumers(String qce) {
		this.queueConsumers.forEach((name, consumer) -> {
			System.out.println(name);
			if (name.equals(qce)) {
				logger.debug("start a specific queueConsumer");
				runQueueConsumer(consumer);
			}
		});
	}

	private void runQueueConsumer(QueueConsumerEncapsulator consumer) {
		QueueConsumer[] consumerThreads = consumer.getThreads();
		for (int i = 0; i < consumerThreads.length; i++) {
			if (!consumerThreads[i].isThreadStarted()) {
				System.out.println("running a queue consumer instance"+ i+1);
				logger.debug("running a queue consumer instance"+ i+1);
				consumerThreads[i].startQueueConsumer();
			}
		}
	}

	private void stopSpecificQueueConsumers(String qce) {
		this.queueConsumers.forEach((name, consumer) -> {
			if (name.equals(qce)) {
				logger.debug("stoping specific queue consumer");
				cancelRunningQueueConsumer(consumer);
			}
		});
	}

	private void cancelRunningQueueConsumer(QueueConsumerEncapsulator consumer) {
		QueueConsumer[] consumerThreads = consumer.getThreads();
		for (int i = 0; i < consumerThreads.length; i++) {
			logger.debug("canceling queue consumer instance "+ i+1);
			consumerThreads[i].stopQueueConsumer();
		}
	}

	private void startAllQueueConsumers() {
		logger.debug("startign all queue consumers");
		this.queueConsumers.forEach((name, consumer) -> {
			runQueueConsumer(consumer);
		});
	}

	class QueueConsumerEncapsulator {

		private int instancesTotal;
		private QueueConsumer queueConsumer;
		private QueueConsumer[] arrayConsumers;

		public QueueConsumerEncapsulator(QueueConsumer consumer, int instances) {
			arrayConsumers = new QueueConsumer[instances];
			for (int i = 0; i < instances; i++) {
				arrayConsumers[i] = SerializationUtils.clone(consumer);
				arrayConsumers[i].setConnectionFactory(consumer.getConnectionFactory());
				arrayConsumers[i].setConsumer(consumer.getConsumer());
				
			}
			this.queueConsumer = consumer;
			this.instancesTotal = instances;
		}

		protected int getInstancesTotal() {
			return instancesTotal;
		}

		protected void setInstancesTotal(int instances) {
			this.instancesTotal = instances;
		}

		protected String getQueueConsumerName() {
			return queueConsumer.getClass().getSimpleName();
		}

		protected QueueConsumer getQueueConsumer() {
			return queueConsumer;
		}

		protected void setQueueConsumer(QueueConsumer queueConsumer) {
			this.queueConsumer = queueConsumer;
		}

		protected QueueConsumer[] getThreads() {
			return this.arrayConsumers;
		}

		@Override
		public int hashCode() {
			final String queueConsumerName = queueConsumer.getClass().getSimpleName();
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + instancesTotal;
			result = prime * result + ((queueConsumerName == null) ? 0 : queueConsumerName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			final String queueConsumerName = queueConsumer.getClass().getSimpleName();
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			QueueConsumerEncapsulator other = (QueueConsumerEncapsulator) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (instancesTotal != other.instancesTotal)
				return false;
			if (queueConsumerName == null) {
				if (other.queueConsumer.getClass().getSimpleName() != null)
					return false;
			} else if (!queueConsumerName.equals(other.queueConsumer.getClass().getSimpleName()))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return queueConsumer.getClass().getSimpleName();
		}

		private QueueConsumerManager getEnclosingInstance() {
			return QueueConsumerManager.this;
		}

	}

}
