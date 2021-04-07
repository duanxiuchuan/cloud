package com.erongdu.server.activiti.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.erongdu.common.core.entity.activiti.IdGroup;
import com.erongdu.server.activiti.dao.IdGroupDao;
import com.erongdu.server.activiti.service.IdGroupService;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-28
 */
 
@Service("idGroupService") 
public class IdGroupServiceImpl implements IdGroupService{
	
    @Resource
    private IdGroupDao idGroupDao;

	@Override
	public int insert(IdGroup entity) {
		return idGroupDao.insert(entity);
	}

	@Override
	public int delete(Long id) {
		return idGroupDao.deleteById(id);
	}

	@Override
	public int update(IdGroup entity) {
		return idGroupDao.update(entity);
	}
	
	@Override
	public List<IdGroup> list(Map<String, Object> params) {
		return idGroupDao.listSelective(params);
	}

	@Override
	public IdGroup findById(Long id) {
		return idGroupDao.findById(id);
	}
}