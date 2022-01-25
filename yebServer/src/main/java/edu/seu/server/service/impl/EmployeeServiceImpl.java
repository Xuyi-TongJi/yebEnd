package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.config.messageQueue.MessageQueueProperties;
import edu.seu.server.mapper.EmployeeMapper;
import edu.seu.server.mapper.MailLogMapper;
import edu.seu.server.pojo.Employee;
import edu.seu.server.pojo.MailLog;
import edu.seu.server.service.IEmployeeService;
import edu.seu.server.util.MessageConstants;
import edu.seu.server.util.enumUtil.EngageFormUtil;
import edu.seu.server.util.enumUtil.TiptopDegreeUtil;
import edu.seu.server.util.enumUtil.WedlockUtil;
import edu.seu.server.util.enumUtil.WorkStateUtil;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 员工服务实现类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;
    @Resource
    private MailLogMapper mailLogMapper;

    private final RabbitTemplate rabbitTemplate;
    private final MessageQueueProperties messageQueueProperties;

    public EmployeeServiceImpl(RabbitTemplate rabbitTemplate,
                               MessageQueueProperties messageQueueProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageQueueProperties = messageQueueProperties;
    }

    @Override
    public IPage<Employee> getEmployeeByPage(Integer currentPage, Integer pageSize, Employee employee,
                                             LocalDate[] beginDateScopes) {
        Page<Employee> page = new Page<>(currentPage, pageSize);
        return employeeMapper.getEmployeeByPage(page, employee, beginDateScopes);
    }

    @Override
    public Integer addEmployee(Employee employee) {
        if (!validityCheck(employee)) {
            return -1;
        }
        addTimeProperty(employee);
        employee.setWorkID(getMaxWorkId());
        int result = employeeMapper.insert(employee);
        if (result == 1) {
            // 添加员工成功，发送该员工对象给消息队列
            sendMessage(employee);
        }
        return result;
    }

    @Override
    public Integer updateEmployee(Employee employee) {
        if (!validityCheck(employee)) {
            return -1;
        }
        addTimeProperty(employee);
        return employeeMapper.updateById(employee);
    }

    @Override
    public List<Employee> getEmployee(Integer id) {
        return employeeMapper.getEmployee(id);
    }

    /**
     * 消息队列发送消息实现类，开启消息回调机制
     * @param serializable 可序列化的消息
     */
    @Override
    public void sendMessage(Serializable serializable) {
        String msgId = UUID.randomUUID().toString();
        Employee employee = (Employee) serializable;
        MailLog mailLog = new MailLog();
        mailLog.setMsgId(msgId);
        mailLog.setEid(employee.getId());
        mailLog.setStatus(MessageConstants.DELIVERING);
        mailLog.setRouteKey(messageQueueProperties.getConfirmRoutingKeyName());
        mailLog.setExchange(messageQueueProperties.getConfirmExchangeName());
        mailLog.setTryTime(LocalDateTime.now().plusMinutes(MessageConstants.MSG_TIMEOUT));
        mailLog.setCount(0);
        mailLog.setCreateTime(LocalDateTime.now());
        mailLog.setUpdateTime(LocalDateTime.now());
        mailLogMapper.insert(mailLog);
        // 发送至rabbit消息队列，第三个参数为msgId
        rabbitTemplate.convertAndSend(messageQueueProperties.getConfirmRoutingKeyName(),
                employee, new CorrelationData(msgId));
    }

    /**
     * employee实体类参数校验
     *
     * @param employee employee实体类
     * @return 各枚举类参数是否合法
     */
    private boolean validityCheck(Employee employee) {
        return EngageFormUtil.nameIncluded(employee.getEngageForm()) &&
                TiptopDegreeUtil.nameIncluded(employee.getTiptopDegree()) &&
                WedlockUtil.nameIncluded(employee.getWedlock()) &&
                WorkStateUtil.nameIncluded(employee.getWorkState());
    }

    /**
     * 更新/添加日期属性
     * @param employee 待更新/赋值的实体类
     */
    private void addTimeProperty(Employee employee) {
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        long days = beginContract.until(endContract, ChronoUnit.DAYS);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days / 365.00)));
        LocalDate conversionTime = employee.getConversionTime();
        days = conversionTime.until(LocalDate.now(), ChronoUnit.DAYS);
        employee.setWorkAge(Integer.parseInt(String.valueOf(days / 365)));
    }

    /**
     * 获得下一个添加的员工的workId
     * @return workId的String值
     */
    private String getMaxWorkId() {
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workID)"));
        return String.format("%08d", Integer.parseInt(maps.get(0).get("max(workID)").toString()) + 1);
    }

}
