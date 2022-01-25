package edu.seu.server.config.messageQueue;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.seu.server.pojo.MailLog;
import edu.seu.server.service.IMailLogService;
import edu.seu.server.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMq 配置类
 * 采用备份交换机做消息确认
 * @author xuyitjuseu
 */
@Configuration
@Slf4j
public class RabbitMqConfiguration {

    private final CachingConnectionFactory connectionFactory;
    private final IMailLogService mailLogService;
    private final RabbitAdmin rabbitAdmin;
    private final MessageQueueProperties messageQueueProperties;

    public RabbitMqConfiguration(CachingConnectionFactory connectionFactory,
                                 IMailLogService mailLogService,
                                 RabbitAdmin rabbitAdmin,
                                 MessageQueueProperties messageQueueProperties) {
        this.connectionFactory = connectionFactory;
        this.mailLogService = mailLogService;
        this.rabbitAdmin = rabbitAdmin;
        this.messageQueueProperties = messageQueueProperties;
    }

    @Bean
    public RabbitTemplate getRabbitTemplate() {
        RabbitTemplate rabbitTemplate =  new RabbitTemplate(connectionFactory);
        // 消息确认回调,确认消息是否到达broker correlationData为唯一标识， ack为是否确认成功的布尔类型，cause为失败原因
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            assert correlationData != null;
            String msgId = correlationData.getId();
            if (ack) {
                log.info("{}, 消息发送成功", msgId);
                mailLogService.update(new UpdateWrapper<MailLog>()
                        .set("status", MessageConstants.SUCCESS).eq("msgId", msgId));
            } else {
                log.error("{},消息发送失败", msgId);
            }
        });
        // 消息失败回调
        rabbitTemplate.setReturnCallback((message, respCode, respText, exchange, routingKey) -> {
            log.error("{}, 消息发送失败", message);
        });
        return rabbitTemplate;
    }

    @Bean("CONFIRM_QUEUE")
    public Queue getConfirmQueue() {
        return QueueBuilder.durable(messageQueueProperties.getConfirmQueueName()).build();
    }

    @Bean("CONFIRM_EXCHANGE")
    public DirectExchange getConfirmExchange() {
        return ExchangeBuilder.directExchange(messageQueueProperties.getConfirmExchangeName())
                .durable(true).build();
    }

    @Bean
    public Binding confirmQueueBindingConfirmExchange(@Qualifier("CONFIRM_QUEUE") Queue queue,
                                                      @Qualifier("CONFIRM_EXCHANGE") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(messageQueueProperties.getConfirmRoutingKeyName()).noargs();
    }

    /**
     * 创建交换机和队列
     */
    @Bean
    public void createExchangeAndQueue() {
        rabbitAdmin.declareExchange(getConfirmExchange());
        rabbitAdmin.declareQueue(getConfirmQueue());
    }
}
