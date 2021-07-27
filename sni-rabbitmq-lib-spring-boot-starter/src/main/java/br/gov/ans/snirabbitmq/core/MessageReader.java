package br.gov.ans.snirabbitmq.core;

import java.io.Serializable;

import com.rabbitmq.client.Channel;

public interface MessageReader extends Serializable {
	public String getReaderName();
	public MessageConverter getMessageConverter();
	public void readMessage(Channel channel);
	public void acknowledgeMode(AcknowledgeMode mode);
}
