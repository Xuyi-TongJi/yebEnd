package edu.seu.server;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.seu.server.common.vo.AdminUpdateVo;
import edu.seu.server.config.messageQueue.MessageQueueProperties;
import edu.seu.server.config.swagger.Swagger2Config;
import edu.seu.server.mapper.DepartmentMapper;
import edu.seu.server.mapper.EmployeeMapper;
import edu.seu.server.mapper.MenuRoleMapper;
import edu.seu.server.pojo.*;
import edu.seu.server.service.IAdminService;
import edu.seu.server.service.IMenuService;
import edu.seu.server.service.IRoleService;
import edu.seu.server.util.RedisUtil;
import org.dozer.Mapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = YebServerApplication.class)
@RunWith(value = SpringRunner.class)
public class YebTest {

    @Autowired
    Mapper mapper;

    @Autowired
    IAdminService adminService;

    @Autowired
    Swagger2Config swagger2Config;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    IRoleService roleService;

    @Autowired
    IMenuService menuService;

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    ServerProperties serverProperties;

    @Autowired
    MessageQueueProperties messageQueueProperties;

    @Autowired
    MenuRoleMapper menuRoleMapper;

    @Test
    public void test01() {
        Admin admin = adminService.getAdminByUsername("xxxxxx");
        Assert.assertNull(admin);
    }

    @Test
    public void test02() {
        System.out.println(swagger2Config.getContactAddress()+ "/doc.html");
    }

    @Test
    public void test03() {
        List<Role> roleListByAdminId = adminService.getRoleListByAdminId(2);
        for (Role role: roleListByAdminId) {
            System.out.println(role);
        }
    }

    @Test
    public void test05() {
        List<Integer> ridList = (List<Integer>)redisTemplate.opsForValue().get(RedisUtil.MENU_ID_LIST);
        assert ridList != null;
        for (Integer i:
             ridList) {
            System.out.println(i);
        }
    }

    @Test
    public void test06() {
        redisTemplate.delete(RedisUtil.MENU_ID_LIST);
        List<Integer> midList = menuService.getMidList();
        for (Integer i:
             midList) {
            System.out.print(i + " ");
        }
    }

    @Test
    public void test07() {
        Department department = new Department();
        department.setResult(-1);
        department.setParentId(-1);
        department.setEnabled(true);
        department.setId(-1);
        department.setName("测试部门");
        departmentMapper.addDepartment(department);
        System.out.println(department.getResult());
    }

    @Test
    public void test08() {
        AdminUpdateVo admin = new AdminUpdateVo();
        admin.setId(2);
        admin.setEnabled(false);
        Admin admin1 = mapper.map(admin, Admin.class);
        admin1.setEnabled(admin.getEnabled());
        adminService.updateById(admin1);
    }

    @Test
    public void test09() {
        List<Menu> ids = menuService.list(new QueryWrapper<Menu>().select("id"));
        for (Menu menu : ids) {
            System.out.println(menu);
        }
    }

    @Test
    public void test10() {
        Employee em = employeeMapper.selectOne(new QueryWrapper<Employee>().eq("id", "1"));
        System.out.println(em);
    }

    @Test
    public void test11() {
        List<Employee> employee = employeeMapper.getEmployee(1);
        System.out.println(employee.get(0));
    }

    @Test
    public void test12() {
        System.out.println(serverProperties.getPort());
    }

    @Test
    public void test13() {
        System.out.println(messageQueueProperties.getConfirmQueueName());
    }

    @Test
    public void test14() {
        Map<String, Integer> map = new HashMap<>(3);
        map.put("rid", 8);
        map.put("mid", 7);
        map.put("result", null);
        menuRoleMapper.addMenuRole(map);
        System.out.println(map.get("result"));
    }

    @Test
    public void test15() {
        Map<String, Integer> map = new HashMap<>(3);
        map.put("rid", 8);
        map.put("mid", 7);
        map.put("result", null);
        menuRoleMapper.deleteMenuRole(map);
        System.out.println(map.get("result"));
    }

    @Test
    public void test16() {
        menuService.cleanupCache();
        menuService.getMidList().forEach(System.out::println);
    }
}
