package edu.seu.server.util.enumUtil;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 职称等级枚举类
 * @author xuyitjuseu
 */
public enum LevelTitleUtil {

    /**
     * 所有职称的枚举
     */
    SENIOR("正高级"),
    SUB_SENIOR("副高级"),
    INTERMEDIATE("中级"),
    JUNIOR("初级");

    private final String levelTitleName;

    LevelTitleUtil(String levelTitleName) {
        this.levelTitleName = levelTitleName;
    }

    public String getLevelTitleName() {
        return levelTitleName;
    }

    /**
     * 判断某字符串是否属于所有职称枚举的名称
     * @param str 待判断的字符串
     * @return 布尔值
     */
    public static boolean levelTitleIncluded(@NotNull String str) {
        for (LevelTitleUtil levelTitle: LevelTitleUtil.values()) {
            if (levelTitle.getLevelTitleName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取所有职称级别名称
     */
    public static List<String> getLevelTitleNameList() {
        List<String> results = new ArrayList<>();
        for (LevelTitleUtil levelTitle: LevelTitleUtil.values()) {
            results.add(levelTitle.getLevelTitleName());
        }
        return results;
    }
}
