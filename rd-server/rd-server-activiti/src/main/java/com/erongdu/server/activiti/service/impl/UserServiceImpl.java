package com.erongdu.server.activiti.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erongdu.common.core.entity.system.SystemUser;
import com.erongdu.server.activiti.dao.UserMapper;
import com.erongdu.server.activiti.service.IUserService;

import lombok.RequiredArgsConstructor;

/**
 * @author lh@erongdu.com
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserServiceImpl extends ServiceImpl<UserMapper, SystemUser> implements IUserService {

    @Override
    public SystemUser findByName(String username) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUsername, username);
        return this.baseMapper.selectOne(queryWrapper);
    }

}
