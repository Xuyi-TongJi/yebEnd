package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.mapper.AdminMapper;
import edu.seu.server.mapper.AdminRoleMapper;
import edu.seu.server.pojo.Admin;
import edu.seu.server.pojo.AdminRole;
import edu.seu.server.pojo.Role;
import edu.seu.server.service.IAdminService;
import edu.seu.server.service.IRoleService;
import edu.seu.server.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

    private final IRoleService roleService;
    private final RedisTemplate<String, Object> redisTemplate;

    public AdminServiceImpl(IRoleService roleService,
                            RedisTemplate<String, Object> redisTemplate) {
        this.roleService = roleService;
        this.redisTemplate = redisTemplate;
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
        if (!aidIncluded(aid)) {
            return -2;
        }
        if (null != rIds && rIds.length != 0 && !ridIncluded(rIds)) {
            return -2;
        }
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

    private boolean ridIncluded(Integer ... rIds) {
        List<Integer> ridList = roleService.getRidList();
        for (Integer ridToAdd:
             rIds) {
            if (!ridList.contains(ridToAdd)) {
                return false;
            }
        }
        return true;
    }

    private boolean aidIncluded(Integer adminId) {
        String keyName = RedisUtil.ADMIN_ID_LIST;
        List<Integer> adminIdList = (List<Integer>)redisTemplate.opsForValue().get(keyName);
        if (CollectionUtils.isEmpty(adminIdList)) {
            adminIdList = list(new QueryWrapper<Admin>().select("id"))
                    .stream().map(Admin::getId).collect(Collectors.toList());
            redisTemplate.opsForValue().set(keyName, adminIdList);
        }
        return adminIdList.contains(adminId);
    }

    @Override
    public void cleanupCache() {
        redisTemplate.delete(RedisUtil.ADMIN_ID_LIST);
    }
}
