package edu.seu.server;

import edu.seu.server.config.swagger.Swagger2Config;
import edu.seu.server.pojo.Admin;
import edu.seu.server.pojo.Role;
import edu.seu.server.service.IAdminService;
import edu.seu.server.service.IMenuService;
import edu.seu.server.service.IRoleService;
import edu.seu.server.util.RedisUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

@SpringBootTest(classes = YebServerApplication.class)
@RunWith(value = SpringRunner.class)
public class YebTest {

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
    public void test04() {
        List<Integer> list = (List<Integer>)redisTemplate.opsForValue().get(RedisUtil.ROLE_ID_LIST);
        Assert.assertTrue(CollectionUtils.isEmpty(list));
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
}