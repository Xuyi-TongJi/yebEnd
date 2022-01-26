package edu.seu.server;

import edu.seu.server.common.vo.AdminUpdateVo;
import edu.seu.server.pojo.Admin;
import edu.seu.server.util.enumUtil.EngageFormUtil;
import edu.seu.server.util.enumUtil.LevelTitleUtil;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void test03() {
        Mapper mapper = new DozerBeanMapper();
        AdminUpdateVo admin = new AdminUpdateVo();
        admin.setId(2);
        admin.setEnabled(false);
        Admin admin1 = mapper.map(admin, Admin.class);
        System.out.println(admin);
        System.out.println(admin1);
    }

    @Test
    public void test04() {
        Integer[] rIds = null;
        List<Integer> ridList = new ArrayList<>(3);
        ridList.set(0, 1);
        ridList.set(1, 2);
        ridList.set(2, 3);
        for (Integer ridToAdd:
                rIds) {
            if (!ridList.contains(ridToAdd)) {
                System.out.println("false!");
            }
        }
    }

    @Test
    public void test05() {
        long days = 732;
        System.out.println(Integer.parseInt(String.valueOf(days / 365)));
    }

    @Test
    public void test06() {
        Arrays.stream(EngageFormUtil.values()).map(EngageFormUtil::getFormName).forEach(System.out::println);
    }

    @Test
    public void test07() {
        Integer a = 1;
        Integer b = 1;
        System.out.println(a == b);
    }
}
