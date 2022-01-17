package edu.seu.server.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.seu.server.pojo.ResponseBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当访问接口没有权限时，自定义返回结果
 * @author xuyitjuseu
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        ResponseBean bean = ResponseBean.error("403", "您的权限不足！", null);
        PrintWriter pw = httpServletResponse.getWriter();
        pw.write(new ObjectMapper().writeValueAsString(bean));
        pw.flush();
        pw.close();
    }
}
