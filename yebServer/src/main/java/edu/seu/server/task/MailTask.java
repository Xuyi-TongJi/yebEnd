package edu.seu.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.seu.server.config.messageQueue.MessageQueueProperties;
import edu.seu.server.pojo.Employee;
import edu.seu.server.pojo.MailLog;
import edu.seu.server.service.IEmployeeService;
import edu.seu.server.service.IMailLogService;
import edu.seu.server.util.MessageConstants;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件定时发送任务
 * @author xuyitjuseu
 */
// @Component
public class MailTask {

    private final IMailLogService mailLogService;
    private final IEmployeeService employeeService;
    private final RabbitTemplate rabbitTemplate;
    private final MessageQueueProperties messageQueueProperties;

    public MailTask(IMailLogService mailLogService,
                    IEmployeeService employeeService,
                    RabbitTemplate rabbitTemplate,
                    MessageQueueProperties messageQueueProperties) {
        this.mailLogService = mailLogService;
        this.employeeService = employeeService;
        this.rabbitTemplate = rabbitTemplate;
        this.messageQueueProperties = messageQueueProperties;
    }

    /**
     * 邮件发送定时任务,每隔十秒执行一次
     * 需要在启动类中开启定时任务@EnableScheduing
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask() {
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>()
                .eq("status", MessageConstants.DELIVERING)
                // 重试时间less than当前时间
                .lt("tryTime", LocalDateTime.now()));
        list.forEach(mailLog -> {
            if (mailLog.getCount() >= MessageConstants.MAX_TRY_COUNT) {
                mailLogService.update(new UpdateWrapper<MailLog>()
                        .set("status", MessageConstants.FAILURE).eq("msgId", mailLog.getMsgId()));
            }
            mailLogService.update(new UpdateWrapper<MailLog>()
                    .set("count", mailLog.getCount() + 1)
                    .set("updateTime", LocalDateTime.now())
                    .set("tryTime", LocalDateTime.now().plusMinutes(MessageConstants.MSG_TIMEOUT))
                    .eq("msgId", mailLog.getMsgId()));
            // 获取Employee实体类
            Employee employee = employeeService.getEmployee(mailLog.getEid()).get(0);
            // 发送消息至消息队列
            rabbitTemplate.convertAndSend(
                    messageQueueProperties.getConfirmExchangeName(),
                    messageQueueProperties.getConfirmRoutingKeyName(),
                    employee,
                    new CorrelationData(mailLog.getMsgId()));
        });
    }
}
