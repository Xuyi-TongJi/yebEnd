package edu.seu.server.exception;

import edu.seu.server.common.lang.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 全局异常处理器
 * @author xuyitjuseu
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseBean handle(RuntimeException exception) {
        log.error("运行时异常:{}", exception.getMessage());
        return ResponseBean.error(400, "服务器进行响应时发生异常，请重试", null);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLException.class)
    public ResponseBean handle(SQLException exception) {
        log.error("服务器内部异常:{}", exception.getMessage());
        return ResponseBean.error(500, "数据库异常，操作失败!", null);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseBean handle(AccessDeniedException exception) {
        log.error("权限不足异常:{}", exception.getMessage());
        return ResponseBean.error(403, "您的权限不足，无法进行响应操作", null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseBean handle(UsernameNotFoundException exception) {
        log.error("登录异常:{}", exception.getMessage());
        return ResponseBean.error(400, "无法找到响应用户名！", null);
    }
}
