package edu.seu.server.util.enumUtil;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 最高学历工具类
 * @author xuyitjuseu
 */

public enum TiptopDegreeUtil {
    /**
     * 最高学历枚举
     */
    PRIMARY_SCHOOL("离异"),
    MIDDLE_SCHOOL("未婚"),
    HIGH_SCHOOL("已婚"),
    BACHELOR("学士"),
    MASTER("硕士"),
    PHD("博士");

    @Getter
    private final String name;

    TiptopDegreeUtil(String name) {
        this.name = name;
    }

    public static boolean nameIncluded(String name) {
        for (TiptopDegreeUtil tiptopDegree:
                TiptopDegreeUtil.values()) {
            if (name.equals(tiptopDegree.getName())) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getList() {
        return Arrays.stream(TiptopDegreeUtil.values())
                .map(TiptopDegreeUtil::getName).collect(Collectors.toList());
    }
}
