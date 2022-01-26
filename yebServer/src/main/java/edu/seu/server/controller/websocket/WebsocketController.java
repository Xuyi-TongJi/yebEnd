package edu.seu.server.controller.websocket;

import edu.seu.server.pojo.Admin;
import edu.seu.server.pojo.websocket.ChatMsg;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * WebSocket前端控制器
 * @author xuyitjuseu
 */
@RestController
public class WebsocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebsocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMsg chatMsg) {
        Admin admin = (Admin)authentication.getPrincipal();
        chatMsg.setFrom(admin.getUsername());
        chatMsg.setFormNickName(admin.getName());
        chatMsg.setDate(LocalDateTime.now());
        // 发送消息 to(收件人)已经由前端设置, /queue/chat为配置类中配置的代理域
        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(), "/queue/chat", chatMsg);
    }
}
