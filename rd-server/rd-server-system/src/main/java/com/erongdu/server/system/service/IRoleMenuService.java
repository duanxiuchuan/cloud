package com.erongdu.server.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erongdu.common.core.entity.system.RoleMenu;

import java.util.List;

/**
 * @author erongdu.com
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    /**
     * 删除角色菜单关联数据
     *
     * @param roleIds 角色id
     */
    void deleteRoleMenusByRoleId(String[] roleIds);

    /**
     * 删除角色菜单关联数据
     *
     * @param menuIds 菜单id
     */
    void deleteRoleMenusByMenuId(String[] menuIds);

    /**
     * 获取角色对应的菜单列表
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    List<RoleMenu> getRoleMenusByRoleId(String roleId);
}
