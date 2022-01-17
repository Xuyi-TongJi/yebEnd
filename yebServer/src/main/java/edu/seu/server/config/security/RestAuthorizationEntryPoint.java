package edu.seu.server.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.seu.server.pojo.ResponseBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当未登录或者token失效时访问接口时，自定义返回结果
 * @author xuyitjuseu
 */
@Component
public class RestAuthorizationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        PrintWriter pw = httpServletResponse.getWriter();
        ResponseBean bean = ResponseBean.error("401", "尚未登录，请登录！", null);
        pw.write(new ObjectMapper().writeValueAsString(bean));
        pw.flush();
        pw.close();
    }
}
