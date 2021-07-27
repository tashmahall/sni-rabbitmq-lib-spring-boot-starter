package br.gov.ans.snirabbitmq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ans.snirabbitmq")
public class SNIRabbitMQANSProperties {
	private String host;
	
	private int port;
	
	private String virtualHost;
	
	private String rabbitmqUsername;
	
	private String rabbitmqPassword;
	
	private String sslAlgorithm;
	
	private String trustStoreLocation;
	
	private String trustStoreType;
	
	private String trustStorePassword;
	
	private String sniHostName;

	private int replyTimeout;
	
	private int consumeTimeout;
	
	private int connectionTimeout;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	public String getRabbitmqUsername() {
		return rabbitmqUsername;
	}

	public void setRabbitmqUsername(String rabbitmqUsername) {
		this.rabbitmqUsername = rabbitmqUsername;
	}

	public String getRabbitmqPassword() {
		return rabbitmqPassword;
	}

	public void setRabbitmqPassword(String rabbitmqPassword) {
		this.rabbitmqPassword = rabbitmqPassword;
	}

	public String getSslAlgorithm() {
		return sslAlgorithm;
	}

	public void setSslAlgorithm(String sslAlgorithm) {
		this.sslAlgorithm = sslAlgorithm;
	}

	public String getTrustStoreLocation() {
		return trustStoreLocation;
	}

	public void setTrustStoreLocation(String trustStoreLocation) {
		this.trustStoreLocation = trustStoreLocation;
	}

	public String getTrustStoreType() {
		return trustStoreType;
	}

	public void setTrustStoreType(String trustStoreType) {
		this.trustStoreType = trustStoreType;
	}

	public String getTrustStorePassword() {
		return trustStorePassword;
	}

	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}

	public String getSniHostName() {
		return sniHostName;
	}

	public void setSniHostName(String sniHostName) {
		this.sniHostName = sniHostName;
	}

	public int getReplyTimeout() {
		return replyTimeout;
	}

	public void setReplyTimeout(int replyTimeout) {
		this.replyTimeout = replyTimeout;
	}

	public int getConsumeTimeout() {
		return consumeTimeout;
	}

	public void setConsumeTimeout(int consumeTimeout) {
		this.consumeTimeout = consumeTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	

}
