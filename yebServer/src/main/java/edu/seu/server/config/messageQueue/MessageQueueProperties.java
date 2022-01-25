package edu.seu.server.config.messageQueue;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列属性配置
 * @author xuyitjuseu
 */
@ConfigurationProperties(prefix = "message-queue")
@Configuration
@Data
public class MessageQueueProperties {

    private String confirmQueueName;
    private String confirmRoutingKeyName;
    private String confirmExchangeName;
}
