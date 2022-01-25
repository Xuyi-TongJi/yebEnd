package edu.seu.server.util;

/**
 * 消息队列常数
 * @author xuyitjuseu
 */
public class MessageConstants {

    /**
     * 消息投递中
     */
    public static final Integer DELIVERING = 0;

    /**
     * 消息投递成功
     */
    public static final Integer SUCCESS = 1;

    /**
     * 消息投递失败（重试达到最大次数仍未投递成功）
     */
    public static final Integer FAILURE = 2;

    /**
     * 最大尝试次数
     */
    public static final Integer MAX_TRY_COUNT = 3;

    /**
     * 消息超时时间，单位：min
     */
    public static final Integer MSG_TIMEOUT = 1;

}
