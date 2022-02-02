package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.mapper.AdminMapper;
import edu.seu.server.mapper.AdminRoleMapper;
import edu.seu.server.pojo.Admin;
import edu.seu.server.pojo.AdminRole;
import edu.seu.server.pojo.Role;
import edu.seu.server.service.IAdminService;
import edu.seu.server.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  管理员服务实现类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private AdminRoleMapper adminRoleMapper;

    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(RedisTemplate<String, Object> redisTemplate,
                            PasswordEncoder passwordEncoder) {
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin getAdminByUsername(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>()
                .eq("username", username).eq("enabled", true));
    }

    @Override
    public List<Role> getRoleListByAdminId(Integer adminId) {
        return adminMapper.getRoleListById(adminId);
    }

    @Override
    public List<Admin> getAdminListByKeywords(String keywords) {
        Integer currentId =
                ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<Admin> adminList = adminMapper.getAdminListByKeywords(keywords, currentId);
        for (Admin admin:
             adminList) {
            admin.setPassword(null);
        }
        return adminList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateAdminRole(Integer aid, Integer ... rIds) {
        int result = adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId", aid));
        if (result >= 0) {
            // 删除成功，清除缓存
            redisTemplate.delete(RedisUtil.MENU_LIST_ADMIN_PREFIX + aid);
            if (null == rIds || rIds.length == 0) {
                return result;
            }
            if ((result = adminRoleMapper.insertBatch(aid, rIds)) == rIds.length) {
                return result;
            }
        }
        return -1;
    }

    @Override
    public Integer updatePassword(String currentPassword, String newPassword, Integer aid) {
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("id", aid));
        if (!passwordEncoder.matches(currentPassword, admin.getPassword())){
            return -1;
        } else {
            admin.setPassword(passwordEncoder.encode(newPassword));
            int result =  adminMapper.updateById(admin);
            cleanupCache();
            return result;
        }
    }

    @Override
    public void cleanupCache() {
    }
}
