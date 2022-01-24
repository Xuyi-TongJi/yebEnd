package edu.seu.server.util;

/**
 * Redis工具类，主要用于存储Redis缓冲层的key名称
 * @author xuyitjuseu
 */
public class RedisUtil {

    /**
     * 用户-角色-菜单列表前缀
     */
    public static final String MENU_LIST_ADMIN_PREFIX = "menu_";

    /**
     * 菜单id列表
     */
    public static final String MENU_ID_LIST = "midIdList";

    /**
     * 角色id列表
     */
    public static final String ROLE_ID_LIST = "roleIdList";

    /**
     * 角色列表
     */
    public static final String ROLE_LIST = "roleList";

    /**
     * 菜单-角色关联列表
     */
    public static final String MENU_WITH_ROLE = "menuListWithRole";

    /**
     * 部门列表
     */
    public static final String DEPARTMENT_LIST = "departmentList";

    /**
     * 用户id列表
     */
    public static final String ADMIN_ID_LIST = "adminIdList";

    /**
     * 直至面貌列表
     */
    public static final String POLITIC_STATUS_LIST = "politicStatusList";

    /**
     * 职称列表
     */
    public static final String JOB_LEVEL_LIST = "jobLevelList";

    /**
     * 民族列表
     */
    public static final String NATION_LIST = "nationList";

    /**
     * 职位列表
     */
    public static final String POSITION_LIST = "positionList";

}
