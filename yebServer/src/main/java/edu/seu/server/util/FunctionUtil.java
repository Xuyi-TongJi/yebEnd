package edu.seu.server.util;

import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.pojo.Role;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能型功能类
 * @author xuyitjuseu
 */
public class FunctionUtil {

    private static final String PREFIX = "ROLE_";
    private static final String ENGLISH_REGEX = "^[a-zA-Z]+$";
    private static final String CHINESE_REGEX = "^[\u4e00-\u9fa5]+$";

    /**
     * 整形数组去重
     * @param mIds 待去重的整形数组
     * @return 待去重的整形数组
     */
    public static Integer[] distinct(Integer... mIds) {
        List<Integer> collect = Arrays.stream(mIds).distinct().collect(Collectors.toList());
        Integer[] results = new Integer[collect.size()];
        int length = results.length;
        for (int i = 0; i < length; i++) {
            results[i] = collect.get(i);
        }
        return results;
    }

    /**
     * 验证角色名称信息是否满足相应的正则表达式
     * @param role 角色实体类
     * @return 如果不满足直接返回给（前端）响应信息，否则返回null继续执行业务逻辑
     */
    public static ResponseBean validRoleName(Role role) {
        if (!role.getName().matches(ENGLISH_REGEX)) {
            return ResponseBean.error(400, "角色名称仅支持英文字母，不支持其他字符！", null);
        }
        if (!role.getNameZh().matches(CHINESE_REGEX)) {
            return ResponseBean.error(400, "角色中文名称仅支持中文字母，不支持其他字符！", null);
        }
        return null;
    }

    /**
     * 给角色英文名称加入前缀
     * @param role 角色实体类
     */
    public static void addRoleNamePrefix(Role role) {
        if (!role.getName().startsWith(PREFIX)) {
            String name = role.getName();
            role.setName(PREFIX + name);
        }
    }
}
