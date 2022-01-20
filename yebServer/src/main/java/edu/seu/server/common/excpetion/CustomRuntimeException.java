package edu.seu.server.common.excpetion;

/**
 * 自定义的运行时异常
 * @author xuyitjuseu
 */
public class CustomRuntimeException extends RuntimeException{
    public CustomRuntimeException(String message) {
        super(message);
    }
}
