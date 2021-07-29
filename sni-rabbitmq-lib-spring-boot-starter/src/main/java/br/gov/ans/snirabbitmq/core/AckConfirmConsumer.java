package br.gov.ans.snirabbitmq.core;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

public interface AckConfirmConsumer extends Consumer {
	void setChannel(Channel channel);
	Channel getChannel();
	public void setMessageConverter(MessageConverter messageConverter);
	public MessageConverter getMessageConverter();
}
