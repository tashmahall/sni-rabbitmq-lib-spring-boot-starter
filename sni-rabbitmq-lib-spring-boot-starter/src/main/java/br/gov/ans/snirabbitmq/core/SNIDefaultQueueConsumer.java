package br.gov.ans.snirabbitmq.core;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

public class SNIDefaultQueueConsumer extends DefaultConsumer{

	private MessageReader messageReader;
	public SNIDefaultQueueConsumer(Channel channel, MessageReader messageReader) {
		super(channel);

	}
	public MessageReader getMessageReader() {
		return this.messageReader;
	}


	


}
