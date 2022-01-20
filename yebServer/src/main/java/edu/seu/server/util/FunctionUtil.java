package edu.seu.server.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能型功能类
 * @author xuyitjuseu
 */
public class FunctionUtil {

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
}
