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

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final RedisTemplate<String, Object> redisTemplate;

    public MenuRoleServiceImpl(MenuRoleMapper menuRoleMapper,
                               IMenuService menuService,
                               IRoleService roleService,
                               RedisTemplate<String, Object> redisTemplate) {
        this.menuRoleMapper = menuRoleMapper;
        this.menuService = menuService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    public ResponseBean updateMenuRole(Integer rid, Integer... mIds) {
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

    @Override
    public ResponseBean addMenuRole(Integer rid, Integer mid) {
        if (null != mid && mIdsNorIncluded(mid)) {
            return ResponseBean.error(500, "非法的菜单参数!", null);
        }
        Map<String, Integer> map = getParamMap(rid, mid);
        menuRoleMapper.addMenuRole(map);
        int result = map.get("result");
        if (result == 1) {
            cleanupCache();
            return ResponseBean.success("更新成功!", null);
        } else if (result == -1) {
            return ResponseBean.error(400, "该用户已经具有该权限!", null);
        } else {
            return ResponseBean.error(500, "更新失败!", null);
        }
    }

    @Override
    public ResponseBean deleteMenuRole(Integer rid, Integer mid) {
        if (null != mid && mIdsNorIncluded(mid)) {
            return ResponseBean.error(500, "非法的菜单参数!", null);
        }
        Map<String, Integer> map = getParamMap(rid, mid);
        menuRoleMapper.deleteMenuRole(map);
        int result = map.get("result");
        if (result == 1) {
            cleanupCache();
            return ResponseBean.success("删除成功!", null);
        } else if (result != -1) {
            return ResponseBean.error(400, "该用户不存在该权限!", null);
        } else {
            return ResponseBean.error(500, "删除失败!", null);
        }
    }

    @Override
    public void cleanupCache() {
        redisTemplate.delete(RedisUtil.MENU_WITH_ROLE);
        redisTemplate.delete(RedisUtil.ROLE_LIST_WITH_MENU);
    }

    /**
     * 判断mIds数组中的mId是否全部合法
     * @param mIds mIds数组
     * @return 如果合法则返回true，不合法直接抛出异常
     */
    private boolean mIdsNorIncluded(@NotNull Integer... mIds) {
        if (mIds.length > 1) {
            mIds = FunctionUtil.distinct(mIds);
        }
        List<Integer> midList = menuService.getMidList();
        for (Integer midToAdd : mIds) {
            if (!midList.contains(midToAdd)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 构造mysql存储过程所用的参数map
     * @param rid rid
     * @param mid mid
     * @return 参数map
     */
    private Map<String, Integer> getParamMap(Integer rid, Integer mid) {
        Map<String, Integer> map = new HashMap<>(3);
        map.put("rid", rid);
        map.put("mid", mid);
        map.put("result", null);
        return map;
    }
}