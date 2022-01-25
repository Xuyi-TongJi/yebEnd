package edu.seu.mail.listener;

import com.rabbitmq.client.Channel;
import edu.seu.mail.config.MessageQueueConstants;
import edu.seu.mail.util.RedisUtil;
import edu.seu.server.pojo.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * 消息队列的消息接收类，通过监听rabbitMQ接收消息，并发送邮件
 * 在本项目中，不考虑redis入库操作
 *
 * @author xuyitjuseu
 */
@Component
public class MailReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;
    private final TemplateEngine templateEngine;
    private final RedisTemplate<String, Object> redisTemplate;

    public MailReceiver(JavaMailSender javaMailSender,
                        MailProperties mailProperties,
                        TemplateEngine templateEngine, RedisTemplate<String, Object> redisTemplate) {
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
        this.templateEngine = templateEngine;
        this.redisTemplate = redisTemplate;
    }

    @RabbitListener(queues = MessageQueueConstants.CONFIRM_QUEUE_NAME)
    public void handleEmployee(Message<Employee> message, Channel channel) {
        Employee employee = message.getPayload();
        MessageHeaders headers = message.getHeaders();
        // 获取消息序号
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        // 获取msgId
        String msgId = (String) headers.get("spring_returned_message_correlation");

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            if (redisTemplate.opsForHash().entries(RedisUtil.MAIL_ACK_HASH).containsKey(msgId)) {
                LOGGER.error("消息{}已经被消费", msgId);
                // 手动确认收到消息，tag：消息序号(DELIVERY_TAG)，false: 逐条确认，非多条确认
                channel.basicAck(tag, false);
                return;
            }
            // 邮件内容设置
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            mimeMessageHelper.setTo(employee.getEmail());
            mimeMessageHelper.setSubject("入职欢迎邮件");
            mimeMessageHelper.setSentDate(new Date());
            // 邮件内容, 设置模版引擎，模版引擎参数及模版引擎文件名
            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("jobLevelName", employee.getJobLevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            // 发送邮件
            mimeMessageHelper.setText(mail, true);
            javaMailSender.send(mimeMessage);
            assert msgId != null;
            redisTemplate.opsForHash().put(RedisUtil.MAIL_ACK_HASH, msgId, "OK");
            // 手动确认收到消息
            channel.basicAck(tag, false);
        } catch (Exception e) {
            try {
                // 手动确认否定消息,参数3：是否退回到队列
                channel.basicNack(tag, false, true);
            } catch (IOException ex) {
                LOGGER.error("邮件发送失败:{}", ex.getMessage());
            }
            LOGGER.error("邮件发送失败:{}", e.getMessage());
        }
    }

}
