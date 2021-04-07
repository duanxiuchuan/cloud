package com.erongdu.server.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erongdu.common.core.entity.system.SystemUser;

/**
 * @author lh@erongdu.com
 */
public interface IUserService extends IService<SystemUser> {

    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    SystemUser findByName(String username);

}