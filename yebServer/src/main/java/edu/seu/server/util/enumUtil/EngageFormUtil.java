package edu.seu.server.util.enumUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 合同类型工具类
 * @author xuyitjuseu
 */

public enum EngageFormUtil {

    /**
     * 合同类型枚举
     */
    LABOR_CONTRACT("劳动合同"),
    SERVICE_CONTRACT("劳务合同");

    private final String formName;

    EngageFormUtil(String formName) {
        this.formName = formName;
    }

    public String getFormName() {
        return formName;
    }

    /**
     * 校验输入的合同类型名称是否合法
     * @param formName 待校验的合同类型名称
     * @return 是否合法的布尔值
     */
    public static boolean nameIncluded(String formName) {
        for (EngageFormUtil engageForm:
             EngageFormUtil.values()) {
            if (formName.equals(engageForm.getFormName())) {
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
        return Arrays.stream(EngageFormUtil.values()).map(EngageFormUtil::getFormName).collect(Collectors.toList());
    }
}
