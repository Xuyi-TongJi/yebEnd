package edu.seu.server.controller.websocket;

import edu.seu.server.pojo.Admin;
import edu.seu.server.pojo.websocket.ChatMsg;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * WebSocket前端控制器
 * @author xuyitjuseu
 */
@Controller
public class WebsocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebsocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /**
     * 在线聊天消息发送请求
     * @param authentication 在连接到websocket时获取到的权限对象，使用jwt时在Websocket配置类中实现
     * @param chatMsg 待发送的消息实体类
     */
    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMsg chatMsg) {
        // 这里的authentication是在WebsocketConfiguration中获取的
        Admin admin = (Admin)authentication.getPrincipal();
        chatMsg.setFrom(admin.getUsername());
        chatMsg.setFormNickName(admin.getName());
        chatMsg.setDate(LocalDateTime.now());
        // 发送消息 to(收件人)已经由前端设置, /queue/chat为配置类中配置的代理域
        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(), "/queue/chat", chatMsg);
    }
}
