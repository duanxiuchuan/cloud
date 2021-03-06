package com.erongdu.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erongdu.common.core.entity.system.Menu;

import java.util.List;

/**
 * @author erongdu.com
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取用户权限集
     *
     * @param username 用户名
     * @return 权限集合
     */
    List<Menu> findUserPermissions(String username);
}