package edu.seu.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = MailApplication.class)
@RunWith(value = SpringRunner.class)
public class YebMailTest {

    @Autowired
    ServerProperties serverProperties;

    @Test
    public void test01() {
        System.out.println(serverProperties.getPort());
    }
}
