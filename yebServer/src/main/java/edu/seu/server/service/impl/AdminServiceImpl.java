package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.mapper.AdminMapper;
import edu.seu.server.mapper.MenuMapper;
import edu.seu.server.mapper.MenuRoleMapper;
import edu.seu.server.pojo.Admin;
import edu.seu.server.pojo.Menu;
import edu.seu.server.pojo.Role;
import edu.seu.server.service.IAdminService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    private AdminMapper adminMapper;
    
    @Override
    public Admin getAdminByUsername(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>()
                .eq("username", username).eq("enabled", true));
    }

    @Override
    public List<Role> getRoleListByAdminId(Integer adminId) {
        return adminMapper.getRoleListById(adminId);
    }
}
