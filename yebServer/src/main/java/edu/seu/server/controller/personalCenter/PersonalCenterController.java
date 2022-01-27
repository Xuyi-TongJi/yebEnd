package edu.seu.server.controller.personalCenter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.common.vo.AdminUpdateVo;
import edu.seu.server.pojo.Admin;
import edu.seu.server.service.IAdminService;
import edu.seu.server.util.OssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.dozer.Mapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * 个人中心前端控制器
 * @author xuyitjuseu
 */
@RestController
@RequestMapping("/personalCenter")
@Api(tags = "personalCenterController")
public class PersonalCenterController {

    private final IAdminService adminService;
    private final Mapper mapper;

    public PersonalCenterController(IAdminService adminService,
                                    Mapper mapper) {
        this.adminService = adminService;
        this.mapper = mapper;
    }

    @ApiOperation("更新除用户名密码以外的账号信息")
    @PutMapping("/info")
    public ResponseBean updateAdminInfo(@Validated @RequestBody AdminUpdateVo adminVo, Authentication authentication) {
        // 校验是否操作自己的账号
        Integer loginId = ((Admin)authentication.getPrincipal()).getId();
        if (!loginId.equals(adminVo.getId())) {
            return ResponseBean.error(403, "非法操作", null);
        }
        Admin admin = mapper.map(adminVo, Admin.class);
        admin.setEnabled(adminVo.getEnabled());
        admin.getAuthorities();
        if (adminService.updateById(admin)) {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(admin, null, authentication.getAuthorities()));
            return ResponseBean.success("更新成功！", adminVo);
        } else {
            return ResponseBean.error(500, "更新失败", adminVo);
        }
    }

    /**
     * 更新用户密码，需要验证当前密码
     * @param passwordMap 旧密码键必须为current-password, 新密码键必须为new-password，用户id键为aid
     * @return 公共返回对象
     */
    @ApiOperation("更新用户密码")
    @PutMapping("/password")
    public ResponseBean updatePassword(@RequestBody Map<String, String> passwordMap, Authentication authentication) {
        Integer loginAid = ((Admin)authentication.getPrincipal()).getId();
        Integer aid = Integer.parseInt(passwordMap.get("aid"));
        if (!loginAid.equals(aid)) {
            return ResponseBean.error(403, "非法操作！", null);
        }
        String currentPassword = passwordMap.get("current-password");
        String newPassword = passwordMap.get("new-password");
        Integer result = adminService.updatePassword(currentPassword, newPassword, aid);
        if (result == 1) {
            return ResponseBean.success("更新成功,请重新登录!", null);
        } else if (result == -1){
            return ResponseBean.error(500, "密码错误！", null);
        } else {
            return ResponseBean.error(500, "更新失败！", null);
        }
    }

    @ApiOperation("更新头像")
    @PutMapping("/userFace")
    public ResponseBean updateUserFace(@RequestParam("file")MultipartFile file,
                                       @RequestParam Integer aid,
                                       Authentication authentication) {
        Integer loginAid = ((Admin)authentication.getPrincipal()).getId();
        if (!loginAid.equals(aid)) {
            return ResponseBean.error(403, "非法操作！", null);
        }
        String fileName = OssUtil.uploadImages(file, "userFaces/" + aid);
        String userFaceColumn = "userFace";
        String idColumn = "id";
        if (adminService.update(new UpdateWrapper<Admin>().set(userFaceColumn, fileName).eq(idColumn, aid))) {
            return ResponseBean.success("更新成功！", null);
        } else {
            return ResponseBean.error(500, "更新失败！", null);
        }
    }

    @ApiOperation(value = "获得头像", produces = "image")
    @GetMapping("/userFace")
    public void getUserFace(Authentication authentication, HttpServletResponse response) {
        Integer loginAid = ((Admin)authentication.getPrincipal()).getId();
        String fileName = adminService.list(new QueryWrapper<Admin>().select("userFace").eq("id", loginAid))
                .get(0).getUserFace();
        InputStream is = OssUtil.downloadImages(fileName);
        if (is != null) {
            String[] strings = fileName.split("\\.");
            String suffix = strings[strings.length - 1];
            response.setHeader("Connection", "close");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("image/" + suffix);
            OutputStream pw = null;
            FileInputStream fis = null;
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            try {
                pw = response.getOutputStream();
                // 用文件接收Oss客户端返回的输入流is
                File targetFile = new File(fileName);
                FileUtils.copyInputStreamToFile(is, targetFile);
                fis = new FileInputStream(targetFile);
                while ((bytesRead = fis.read(buffer, 0, buffer.length)) != -1) {
                    pw.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    if (pw != null) {
                        pw.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
