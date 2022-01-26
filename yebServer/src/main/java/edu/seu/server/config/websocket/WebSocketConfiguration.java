package edu.seu.server.config.websocket;

import edu.seu.server.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置类
 * @author xuyitjuseu
 */
@Configuration
@EnableWebSocketMessageBroker
@ConfigurationProperties(prefix = "auth-token")
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private String headerName;

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    public WebSocketConfiguration(JwtTokenUtil jwtTokenUtil,
                                  UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 允许前端通过web连接webSocket服务
     * 可以配置webSocket服务地址以及指定是否使用socketJS
     * @param registry 代理对象
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /* 将ws/ep路径注册为stomp端点，用户连接到了这个端点就可以进行webSocket通讯，支持socketJS，setAllowedOrigins("*")为允许跨域
           withSocketJS为支持socketJS连接 */
        registry.addEndpoint("/ws/ep").setAllowedOrigins("*").withSockJS();
    }

    /**
     * 输入通道参数配置
     * 使用jwt令牌时需要配置
     * @param registration 代理对象
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                assert accessor != null;
                // 判断是否为连接，如果是，需要获取token，并在SecurityContextHolder没有用户对象时设置用户对象
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // 获得前端发送的请求头名称以获取Token
                    String token = accessor.getFirstNativeHeader(headerName);
                    if (!StringUtils.isEmpty(token)) {
                        String authToken = token.substring(jwtTokenUtil.getTokenHead().length());
                        String username = jwtTokenUtil.getUsernameByToken(authToken);
                        // 如果不存在用户名，即没有登录
                        if (!StringUtils.isEmpty(username)) {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            // 验证token是否有效
                            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                                UsernamePasswordAuthenticationToken authentication
                                        = new UsernamePasswordAuthenticationToken(userDetails, null,
                                        userDetails.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                            }
                        }
                    }
                }
                return message;
            }
        });
    }

    /**
     * 配置消息代理
     * @param registry 代理对象
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 配置代理域，可以配置多个，配置代理目的地前缀为/queue(前端),可以在配置域上向客户端推送消息
        registry.enableSimpleBroker("/queue");

    }
}
