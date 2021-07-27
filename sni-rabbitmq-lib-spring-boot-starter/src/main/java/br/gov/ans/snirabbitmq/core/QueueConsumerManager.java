package br.gov.ans.snirabbitmq.core;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class QueueConsumerManager implements InitializingBean {

	private final Map<String, QueueConsumerEncapsulator> queueConsumers = new TreeMap<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		startAllQueueConsumers();

	}

	public void addQueueConsumer(QueueConsumer queueConsumer) {
		this.addQueueConsumer(queueConsumer, 1);
	}

	public void addQueueConsumer(QueueConsumer queueConsumer, int instances) {
		Assert.isTrue((instances < 1), "the instances number must be bigger than one");
		QueueConsumerEncapsulator qce = new QueueConsumerEncapsulator(queueConsumer, instances);
		stopSpecificQueueConsumers(queueConsumer.getQueueName());
		queueConsumers.put(qce.getQueueConsumerName(), qce);
		startSpecificQueueConsumers(queueConsumer.getQueueName());
	}

	private void startSpecificQueueConsumers(String qce) {
		this.queueConsumers.forEach((name, consumer) -> {
			if (name.equals(qce)) {
				runQueueConsumer(consumer);
			}
		});
	}

	private void runQueueConsumer(QueueConsumerEncapsulator consumer) {
		QueueConsumer[] consumerThreads = consumer.getThreads();
		for (int i = 0; i < consumerThreads.length; i++) {
			if (!consumerThreads[i].isThreadStarted()) {
				consumerThreads[i].startQueueConsumer();
			}
		}
	}

	private void stopSpecificQueueConsumers(String qce) {
		this.queueConsumers.forEach((name, consumer) -> {
			if (name.equals(qce)) {
				cancelRunningQueueConsumer(consumer);
			}
		});
	}

	private void cancelRunningQueueConsumer(QueueConsumerEncapsulator consumer) {
		QueueConsumer[] consumerThreads = consumer.getThreads();
		for (int i = 0; i < consumerThreads.length; i++) {
			consumerThreads[i].stopQueueConsumer();
		}
	}

	private void startAllQueueConsumers() {
		this.queueConsumers.forEach((name, consumer) -> {
			runQueueConsumer(consumer);
		});
	}

	class QueueConsumerEncapsulator {

		private int instancesTotal;
		private QueueConsumer queueConsumer;
		private QueueConsumer[] threads;

		public QueueConsumerEncapsulator(QueueConsumer consumer, int instances) {
			threads = new QueueConsumer[instances];
			for (int i = 0; i < instances; i++) {
				threads[i] = SerializationUtils.clone(consumer);
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
			return this.threads;
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
