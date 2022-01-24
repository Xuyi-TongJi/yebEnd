package edu.seu.server.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工作状态工具类
 * @author xuyitjuseu
 */
public enum WorkStateUtil {

    /**
     * 合同类型枚举
     */
    ON_THE_JOB("劳动合同"),
    OUT_THE_JOB("劳务合同");

    private final String workStateName;

    WorkStateUtil(String workStateName) {
        this.workStateName = workStateName;
    }

    public String getWorkStateName() {
        return workStateName;
    }

    /**
     * 校验输入的合同类型名称是否合法
     * @param formName 待校验的合同类型名称
     * @return 是否合法的布尔值
     */
    public static boolean formNameIncluded(String formName) {
        for (WorkStateUtil engageForm:
                WorkStateUtil.values()) {
            if (formName.equals(engageForm.getWorkStateName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取可用的合同类型名称
     * @return 合同类型名称列表
     */
    public static List<String> getEngageFormList() {
        return Arrays.stream(WorkStateUtil.values())
                .map(WorkStateUtil::getWorkStateName).collect(Collectors.toList());
    }
}
