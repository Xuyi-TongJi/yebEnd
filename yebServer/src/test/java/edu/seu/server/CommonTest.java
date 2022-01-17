package edu.seu.server;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CommonTest {
    @Test
    public void test01() {
        String encode = new BCryptPasswordEncoder().encode("123");
        System.out.println(encode);
    }
}
