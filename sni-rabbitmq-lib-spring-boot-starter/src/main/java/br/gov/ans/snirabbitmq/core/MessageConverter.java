package br.gov.ans.snirabbitmq.core;

import java.io.IOException;
import java.io.Serializable;

public interface MessageConverter extends Serializable{
	
	public <T> T fromMessage(RabbitMQMessage message, Class<T> clazz) throws IOException;
	

	public RabbitMQMessage toMessage(Object object, RabbitMQMessageBasicProperties messageProperties) throws IOException;

}
