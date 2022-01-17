package edu.seu.server;

import edu.seu.server.config.swagger.Swagger2Config;
import edu.seu.server.pojo.Admin;
import edu.seu.server.service.IAdminService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = YebServerApplication.class)
@RunWith(value = SpringRunner.class)
public class YebTest {

    @Autowired
    IAdminService adminService;

    @Autowired
    Swagger2Config swagger2Config;

    @Test
    public void test01() {
        Admin admin = adminService.getAdminByUsername("xxxxxx");
        Assert.assertNull(admin);
    }

    @Test
    public void test02() {
        System.out.println(swagger2Config.getContactAddress()+ "/doc.html");
    }
}