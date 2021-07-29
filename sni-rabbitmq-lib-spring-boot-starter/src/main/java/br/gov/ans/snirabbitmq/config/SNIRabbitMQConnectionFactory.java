package br.gov.ans.snirabbitmq.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import com.rabbitmq.client.ConnectionFactory;

import br.gov.ans.snirabbitmq.core.ChannelAndConnectionManager;
@Configuration
public class SNIRabbitMQConnectionFactory implements InitializingBean{
	


	private ConnectionFactory connectionFactory;
	
	@Value("${ans.snirabbitmq.host}")
	private String host;
	
	@Value("${ans.snirabbitmq.port}")
	private int port;
	
	@Value("${ans.snirabbitmq.virtual-host}")
	private String virtualHost;
	
	@Value("${ans.snirabbitmq.rabbitmq-username}")
	private String rabbitMQUsername;
	
	@Value("${ans.snirabbitmq.rabbitmq-password}")
	private String rabbitMQPassword;
	
	@Value("${ans.snirabbitmq.ssl-algorithm}")
	private String sslAlgorithm;
	
	@Value("${ans.snirabbitmq.trust-store-location}")
	private String trustStoreLocation;
	
	@Value("${ans.snirabbitmq.trust-store-type}")
	private String trustStoreType;
	
	@Value("${ans.snirabbitmq.trust-store-password}")
	private String trustStorePassword;
	
	@Value("${ans.snirabbitmq.sni-host-name}")
	private String sniHostName;

	@Value("${ans.snirabbitmq.reply-timeout:5000}")
	private int replyTimeout;
	
	@Value("${ans.snirabbitmq.consume-timeout:10000}")
	private int consumeTimeout;
	
	@Value("${ans.snirabbitmq.connection-timeout:10000}")
	private int connectionTimeout;
	
	private ChannelAndConnectionManager channelAndConnectionManager;
	
	public void afterPropertiesSet() throws Exception {
			Assert.notNull(this.host,message("ans.snirabbitmq.host.name"));
			Assert.notNull(this.port,message("ans.snirabbitmq.host.port"));
			Assert.notNull(this.virtualHost,message("ans.snirabbitmq.virtual-host"));
			Assert.notNull(this.rabbitMQUsername,message("ans.snirabbitmq.rabbitmq-username"));
			Assert.notNull(this.rabbitMQPassword,message("ans.snirabbitmq.rabbitmq-password"));
			Assert.notNull(this.sslAlgorithm,message("ans.snirabbitmq.ssl-algorithm"));
			Assert.notNull(this.trustStoreLocation,message("ans.snirabbitmq.trust-store-location"));
			Assert.notNull(this.trustStoreType,message("ans.snirabbitmq.trust-store-type"));
			Assert.notNull(this.trustStorePassword,message("ans.snirabbitmq.trust-store-password"));
			Assert.notNull(this.sniHostName,message("ans.snirabbitmq.sni-host-name"));
			
			this.connectionFactory = new ConnectionFactory();
			this.connectionFactory.setAutomaticRecoveryEnabled(true);
			this.connectionFactory.setConnectionTimeout(this.connectionTimeout);
			this.connectionFactory.setHandshakeTimeout(this.connectionTimeout);
			this.connectionFactory.setHost(this.host);
			this.connectionFactory.setPassword(this.rabbitMQPassword);
			this.connectionFactory.setPort(this.port);
			SSLContext sslContext = getSSLContext();
			this.connectionFactory.useSslProtocol(sslContext);
			this.connectionFactory.setSocketFactory(sslContext.getSocketFactory());
			this.connectionFactory.setVirtualHost(virtualHost);
			this.connectionFactory.setAutomaticRecoveryEnabled(true);
			this.connectionFactory.setNetworkRecoveryInterval(5000);
			
	}
	private SSLContext getSSLContext() throws GeneralSecurityException, IOException {
        KeyStore tks = KeyStore.getInstance(this.trustStoreType);

        tks.load(this.getTrustStoreFile(), this.trustStorePassword.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(tks);
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
 
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(tks, this.trustStorePassword.toCharArray());
        SSLContext sslContext = SSLContext.getInstance(this.sslAlgorithm);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), secureRandom);
        
        List<SNIServerName> sniHostNames = new ArrayList<>();
        sniHostNames.add(new SNIHostName(this.host));
        sslContext.getDefaultSSLParameters().setServerNames(sniHostNames);
        return sslContext;
	}
	private String message(String property) {
		return "The property ["+property+"] cannot be null";
	}
	private InputStream getTrustStoreFile() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is =classLoader.getResourceAsStream(trustStoreLocation);
        Assert.notNull(is,"Could not find the trust store location");
        return is;
		
	}
	public ConnectionFactory getConnectionFactory(){
		return this.connectionFactory;
	}
	public ChannelAndConnectionManager getChannelAndConnectionManager() {
		if (this.channelAndConnectionManager == null || (!this.channelAndConnectionManager.isChannelOpen()||!this.channelAndConnectionManager.isConnectionOpen()))
			this.channelAndConnectionManager=new ChannelAndConnectionManager(this);
		return this.channelAndConnectionManager;
	}
	@PreDestroy
	public void closeConnectionAndChannel() {
		if (this.channelAndConnectionManager!=null) {
			this.channelAndConnectionManager.closeConnectionAndChannel();
		}
	}
}
