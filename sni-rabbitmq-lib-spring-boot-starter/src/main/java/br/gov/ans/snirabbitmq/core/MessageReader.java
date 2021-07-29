package br.gov.ans.snirabbitmq.core;

import java.io.Serializable;

import com.rabbitmq.client.Channel;

public interface MessageReader extends Serializable {
	public String getReaderName();
	public MessageConverter getMessageConverter();
	public void readMessage(Channel channel);
	public AcknowledgeMode getAcknowledgeMode();
	public String getQueueName();
	public String getConsumerTag();
	public void setConsumerTag(String consumerTag);
}
