package edu.seu.server.controller.websocket;

import edu.seu.server.pojo.Admin;
import edu.seu.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 在线聊天前端控制器
 * @author xuyitjuseu
 */
@RestController
@RequestMapping("/chat")
@Api(tags = "chatController")
public class ChatController {

    private final IAdminService adminService;

    public ChatController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @ApiOperation("获取所有操作员")
    @GetMapping("/admin")
    public List<Admin> getAdminList(String keywords) {
        return adminService.getAdminListByKeywords(keywords);
    }
}
