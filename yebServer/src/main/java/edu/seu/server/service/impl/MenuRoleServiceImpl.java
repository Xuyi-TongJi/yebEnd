package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.mapper.MenuRoleMapper;
import edu.seu.server.pojo.MenuRole;
import edu.seu.server.service.IMenuRoleService;
import edu.seu.server.service.IMenuService;
import edu.seu.server.service.IRoleService;
import edu.seu.server.util.FunctionUtil;
import edu.seu.server.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    private final MenuRoleMapper menuRoleMapper;
    private final IMenuService menuService;
    private final IRoleService roleService;
    private final RedisTemplate<String, Object> redisTemplate;

    public MenuRoleServiceImpl(MenuRoleMapper menuRoleMapper,
                               IMenuService menuService,
                               IRoleService roleService,
                               RedisTemplate<String, Object> redisTemplate) {
        this.menuRoleMapper = menuRoleMapper;
        this.menuService = menuService;
        this.roleService = roleService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean updateMenuRole(Integer rid, Integer... mIds) {
        // 判断rid和mIds是否合法,两参数合法时，在进行相应操作
        if (!ridIncluded(rid)) {
            return ResponseBean.error(500, "非法的角色参数!", null);
        }
        if (null != mIds && mIds.length != 0 && !mIdsIncluded(mIds)) {
            return ResponseBean.error(500, "非法的菜单参数!", null);
        }
        String rIdColumn = "rid";
        if (menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq(rIdColumn, rid)) >= 0) {
            if (null != mIds && mIds.length != 0) {
                // 对mIds进行去重
                mIds = FunctionUtil.distinct(mIds);
                if (menuRoleMapper.insertBatch(rid, mIds) == mIds.length) {
                    cleanupCache();
                    return ResponseBean.success("更新成功!", null);
                }
                return ResponseBean.error(500, "更新失败!", null);
            }
            // 没有mId参数
            cleanupCache();
            return ResponseBean.success("更新成功!", null);
        }
        return ResponseBean.error(500, "更新失败!", null);
    }

    /**
     * 判断rid是否合法
     * @param rid 角色id
     * @return 如果合法则返回true，不合法直接抛出异常
     */
    private boolean ridIncluded(Integer rid) {
        List<Integer> ridList = roleService.getRidList();
        return ridList.contains(rid);
    }

    /**
     * 判断mIds数组中的mId是否全部合法
     * @param mIds mIds数组
     * @return 如果合法则返回true，不合法直接抛出异常
     */
    private boolean mIdsIncluded(Integer... mIds) {
        mIds = FunctionUtil.distinct(mIds);
        List<Integer> midList = menuService.getMidList();
        for (Integer midToAdd : mIds) {
            if (!midList.contains(midToAdd)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void cleanupCache() {
        redisTemplate.delete(RedisUtil.MENU_WITH_ROLE);
    }
}
