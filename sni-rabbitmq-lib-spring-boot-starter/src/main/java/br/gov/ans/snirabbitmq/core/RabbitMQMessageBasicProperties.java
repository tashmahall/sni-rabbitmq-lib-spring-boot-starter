package br.gov.ans.snirabbitmq.core;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.impl.ContentHeaderPropertyReader;
import com.rabbitmq.client.impl.ContentHeaderPropertyWriter;

public class RabbitMQMessageBasicProperties extends BasicProperties implements Serializable{
	
	private static final long serialVersionUID = 1608005226275425495L;
	
	private static final String DEFAULT_ENCODING = Charset.defaultCharset().name();
	public static final String DEFAULT_CONTENT_TYPE = DEFAULT_ENCODING;
    private String contentType = MessagesTypes.CONTENT_TYPE_BYTES;
    private String contentEncoding = DEFAULT_ENCODING;
    private Map<String,Object> headers;
    private Integer deliveryMode = MessageDeliveryMode.PERSISTENT.code;
    private Integer priority = 0;
    private String correlationId;
    private String replyTo;
    private String expiration;
    private String messageId;
    private Date timestamp;
    private String type;
    private String userId;
    private String appId;
    private String clusterId;

    public RabbitMQMessageBasicProperties(
        String contentType,
        String contentEncoding,
        Map<String,Object> headers,
        Integer deliveryMode,
        Integer priority,
        String correlationId,
        String replyTo,
        String expiration,
        String messageId,
        Date timestamp,
        String type,
        String userId,
        String appId,
        String clusterId)
    {
        this.contentType = contentType;
        this.contentEncoding = contentEncoding;
        this.headers = headers==null ? null : Collections.unmodifiableMap(new HashMap<String,Object>(headers));
        this.deliveryMode = deliveryMode;
        this.priority = priority;
        this.correlationId = correlationId;
        this.replyTo = replyTo;
        this.expiration = expiration;
        this.messageId = messageId;
        this.timestamp = timestamp;
        this.type = type;
        this.userId = userId;
        this.appId = appId;
        this.clusterId = clusterId;
    }

    public RabbitMQMessageBasicProperties(DataInputStream in) throws IOException {
        ContentHeaderPropertyReader reader = new ContentHeaderPropertyReader(in);
        boolean contentType_present = reader.readPresence();
        boolean contentEncoding_present = reader.readPresence();
        boolean headers_present = reader.readPresence();
        boolean deliveryMode_present = reader.readPresence();
        boolean priority_present = reader.readPresence();
        boolean correlationId_present = reader.readPresence();
        boolean replyTo_present = reader.readPresence();
        boolean expiration_present = reader.readPresence();
        boolean messageId_present = reader.readPresence();
        boolean timestamp_present = reader.readPresence();
        boolean type_present = reader.readPresence();
        boolean userId_present = reader.readPresence();
        boolean appId_present = reader.readPresence();
        boolean clusterId_present = reader.readPresence();

        reader.finishPresence();

        this.contentType = contentType_present ? reader.readShortstr() : null;
        this.contentEncoding = contentEncoding_present ? reader.readShortstr() : null;
        this.headers = headers_present ? reader.readTable() : null;
        this.deliveryMode = deliveryMode_present ? reader.readOctet() : null;
        this.priority = priority_present ? reader.readOctet() : null;
        this.correlationId = correlationId_present ? reader.readShortstr() : null;
        this.replyTo = replyTo_present ? reader.readShortstr() : null;
        this.expiration = expiration_present ? reader.readShortstr() : null;
        this.messageId = messageId_present ? reader.readShortstr() : null;
        this.timestamp = timestamp_present ? reader.readTimestamp() : null;
        this.type = type_present ? reader.readShortstr() : null;
        this.userId = userId_present ? reader.readShortstr() : null;
        this.appId = appId_present ? reader.readShortstr() : null;
        this.clusterId = clusterId_present ? reader.readShortstr() : null;
    }
    public RabbitMQMessageBasicProperties() {}
    public int getClassId() { return 60; }
    public String getClassName() { return "basic"; }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
           return false;
        RabbitMQMessageBasicProperties that = (RabbitMQMessageBasicProperties) o;
        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null)
            return false;
        if (contentEncoding != null ? !contentEncoding.equals(that.contentEncoding) : that.contentEncoding != null)
            return false;
        if (headers != null ? !headers.equals(that.headers) : that.headers != null)
            return false;
        if (deliveryMode != null ? !deliveryMode.equals(that.deliveryMode) : that.deliveryMode != null)
            return false;
        if (priority != null ? !priority.equals(that.priority) : that.priority != null)
            return false;
        if (correlationId != null ? !correlationId.equals(that.correlationId) : that.correlationId != null)
            return false;
        if (replyTo != null ? !replyTo.equals(that.replyTo) : that.replyTo != null)
            return false;
        if (expiration != null ? !expiration.equals(that.expiration) : that.expiration != null)
            return false;
        if (messageId != null ? !messageId.equals(that.messageId) : that.messageId != null)
            return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null)
            return false;
        if (type != null ? !type.equals(that.type) : that.type != null)
            return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null)
            return false;
        if (appId != null ? !appId.equals(that.appId) : that.appId != null)
            return false;
        if (clusterId != null ? !clusterId.equals(that.clusterId) : that.clusterId != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (contentEncoding != null ? contentEncoding.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (deliveryMode != null ? deliveryMode.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (correlationId != null ? correlationId.hashCode() : 0);
        result = 31 * result + (replyTo != null ? replyTo.hashCode() : 0);
        result = 31 * result + (expiration != null ? expiration.hashCode() : 0);
        result = 31 * result + (messageId != null ? messageId.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (appId != null ? appId.hashCode() : 0);
        result = 31 * result + (clusterId != null ? clusterId.hashCode() : 0);
        return result;
    }

    public Builder builder() {
        Builder builder = new Builder()
            .contentType(contentType)
            .contentEncoding(contentEncoding)
            .headers(headers)
            .deliveryMode(deliveryMode)
            .priority(priority)
            .correlationId(correlationId)
            .replyTo(replyTo)
            .expiration(expiration)
            .messageId(messageId)
            .timestamp(timestamp)
            .type(type)
            .userId(userId)
            .appId(appId)
            .clusterId(clusterId);
        return builder;
    }

    public String getContentType() { return this.contentType; }
    public String getContentEncoding() { return this.contentEncoding; }
    public Map<String,Object> getHeaders() { return this.headers; }
    public Integer getDeliveryMode() { return this.deliveryMode; }
    public Integer getPriority() { return this.priority; }
    public String getCorrelationId() { return this.correlationId; }
    public String getReplyTo() { return this.replyTo; }
    public String getExpiration() { return this.expiration; }
    public String getMessageId() { return this.messageId; }
    public Date getTimestamp() { return this.timestamp; }
    public String getType() { return this.type; }
    public String getUserId() { return this.userId; }
    public String getAppId() { return this.appId; }
    public String getClusterId() { return this.clusterId; }

    public void writePropertiesTo(ContentHeaderPropertyWriter writer)
        throws IOException
    {
        writer.writePresence(this.contentType != null);
        writer.writePresence(this.contentEncoding != null);
        writer.writePresence(this.headers != null);
        writer.writePresence(this.deliveryMode != null);
        writer.writePresence(this.priority != null);
        writer.writePresence(this.correlationId != null);
        writer.writePresence(this.replyTo != null);
        writer.writePresence(this.expiration != null);
        writer.writePresence(this.messageId != null);
        writer.writePresence(this.timestamp != null);
        writer.writePresence(this.type != null);
        writer.writePresence(this.userId != null);
        writer.writePresence(this.appId != null);
        writer.writePresence(this.clusterId != null);

        writer.finishPresence();

        if (this.contentType != null) writer.writeShortstr(this.contentType);
        if (this.contentEncoding != null) writer.writeShortstr(this.contentEncoding);
        if (this.headers != null) writer.writeTable(this.headers);
        if (this.deliveryMode != null) writer.writeOctet(this.deliveryMode);
        if (this.priority != null) writer.writeOctet(this.priority);
        if (this.correlationId != null) writer.writeShortstr(this.correlationId);
        if (this.replyTo != null) writer.writeShortstr(this.replyTo);
        if (this.expiration != null) writer.writeShortstr(this.expiration);
        if (this.messageId != null) writer.writeShortstr(this.messageId);
        if (this.timestamp != null) writer.writeTimestamp(this.timestamp);
        if (this.type != null) writer.writeShortstr(this.type);
        if (this.userId != null) writer.writeShortstr(this.userId);
        if (this.appId != null) writer.writeShortstr(this.appId);
        if (this.clusterId != null) writer.writeShortstr(this.clusterId);
    }

    public void appendPropertyDebugStringTo(StringBuilder acc) {
        acc.append("(content-type=")
           .append(this.contentType)
           .append(", content-encoding=")
           .append(this.contentEncoding)
           .append(", headers=")
           .append(this.headers)
           .append(", delivery-mode=")
           .append(this.deliveryMode)
           .append(", priority=")
           .append(this.priority)
           .append(", correlation-id=")
           .append(this.correlationId)
           .append(", reply-to=")
           .append(this.replyTo)
           .append(", expiration=")
           .append(this.expiration)
           .append(", message-id=")
           .append(this.messageId)
           .append(", timestamp=")
           .append(this.timestamp)
           .append(", type=")
           .append(this.type)
           .append(", user-id=")
           .append(this.userId)
           .append(", app-id=")
           .append(this.appId)
           .append(", cluster-id=")
           .append(this.clusterId)
           .append(")");
    }

}
