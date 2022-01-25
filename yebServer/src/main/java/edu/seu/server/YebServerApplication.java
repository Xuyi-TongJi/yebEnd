package edu.seu.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xuyitjuseu
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("edu.seu.server.mapper")
public class YebServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(YebServerApplication.class, args);
    }
}