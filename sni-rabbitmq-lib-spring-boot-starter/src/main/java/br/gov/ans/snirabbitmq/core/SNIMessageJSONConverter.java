package br.gov.ans.snirabbitmq.core;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SNIMessageJSONConverter implements MessageConverter{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7610630009882200877L;
	private ObjectMapper om = new ObjectMapper();
	@SuppressWarnings("unchecked")
	public <T> T fromMessage(RabbitMQMessage message, Class<T> clazz) throws IOException {
		return (T) om.convertValue(message.getBodyMessage(), Object.class);
	}

	public RabbitMQMessage toMessage(Object entity, RabbitMQMessageBasicProperties messageProperties) throws IOException {
		byte[] entitySerialized = om.writeValueAsBytes(entity);
		RabbitMQMessage message = new RabbitMQMessage();
		message.setBodyMessage(entitySerialized);
		message.setBasicProperties(messageProperties);
		message.setBasicProperties((messageProperties==null?new RabbitMQMessageBasicProperties():messageProperties));
		return message;
	}





}
