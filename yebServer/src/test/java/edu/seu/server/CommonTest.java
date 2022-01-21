package edu.seu.server;

import edu.seu.server.mapper.DepartmentMapper;
import edu.seu.server.pojo.Department;
import edu.seu.server.util.LevelTitleUtil;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CommonTest {
    @Test
    public void test01() {
        String encode = new BCryptPasswordEncoder().encode("123");
        System.out.println(encode);
    }

    @Test
    public void test02() {
        for (LevelTitleUtil levelTitle:
                LevelTitleUtil.values()) {
            System.out.println(levelTitle.getLevelTitleName());
        }
        System.out.println(LevelTitleUtil.levelTitleIncluded("副教授"));
    }

}
