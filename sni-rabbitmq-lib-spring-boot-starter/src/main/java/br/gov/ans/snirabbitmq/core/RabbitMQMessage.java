package br.gov.ans.snirabbitmq.core;

import java.io.Serializable;

import org.springframework.util.Assert;

public class RabbitMQMessage implements  Serializable {
	private static final long serialVersionUID = 433284450760776661L;
	private byte[] bodyMessage;
	private RabbitMQMessageBasicProperties basicProperties;
	public byte[] getBodyMessage() {
		return bodyMessage;
	}
	public void setBodyMessage(byte[] bodyMessage) {
		this.bodyMessage = bodyMessage;
	}
	public void setBodyMessage(String bodyMessage) {
		Assert.notNull(bodyMessage, "The message body cannot be null");
		this.bodyMessage = bodyMessage.getBytes();
	}
	public RabbitMQMessageBasicProperties getBasicProperties() {
		return basicProperties;
	}
	public void setBasicProperties(RabbitMQMessageBasicProperties basicProperties) {
		this.basicProperties = basicProperties;
	}
	

}
