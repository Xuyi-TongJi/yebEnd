package edu.seu.server.util.enumUtil;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 婚姻状况枚举类
 * @author xuyitjuseu
 */
public enum WedlockUtil {

    /**
     * 婚姻状况枚举
     */
    DIVORCED("离异"),
    SPINSTERHOOD("未婚"),
    MARRIED("已婚");

    @Getter
    private final String name;

    WedlockUtil(String name) {
        this.name = name;
    }

    public static boolean nameIncluded(String name) {
        for (WedlockUtil wedlock:
                WedlockUtil.values()) {
            if (name.equals(wedlock.getName())) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getList() {
        return Arrays.stream(WedlockUtil.values())
                .map(WedlockUtil::getName).collect(Collectors.toList());
    }
}
