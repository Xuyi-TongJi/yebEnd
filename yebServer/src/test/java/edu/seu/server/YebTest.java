package edu.seu.server;

import edu.seu.server.util.JwtTokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = YebServerApplication.class)
@RunWith(value = SpringRunner.class)
public class YebTest {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Test
    public void test01() {

    }
}