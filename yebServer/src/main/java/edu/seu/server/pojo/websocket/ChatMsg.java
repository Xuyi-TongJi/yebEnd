package edu.seu.server.pojo.websocket;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * websocket消息实体类
 * @author xuyitjuseu
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class ChatMsg {

    private String to;
    private String from;
    private String content;
    private LocalDateTime date;
    private String formNickName;

}
