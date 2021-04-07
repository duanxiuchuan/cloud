package com.erongdu.server.activiti.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.erongdu.common.core.entity.activiti.IdUser;
import com.erongdu.server.activiti.dao.IdUserDao;
import com.erongdu.server.activiti.service.IdUserService;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-28
 */
 
@Service("idUserService") 
public class IdUserServiceImpl implements IdUserService{
	
    @Resource
    private IdUserDao idUserDao;
	
	@Override
	public List<IdUser> list(Map<String, Object> params) {
		return idUserDao.listSelective(params);
	}

	@Override
	public IdUser findById(String id) {
		return idUserDao.findById(id);
	}
}