package com.erongdu.server.activiti.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.erongdu.common.core.entity.activiti.IdMembership;
import com.erongdu.server.activiti.dao.IdMembershipDao;
import com.erongdu.server.activiti.service.IdMembershipService;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-28
 */
 
@Service("idMembershipService") 
public class IdMembershipServiceImpl implements IdMembershipService{
	
    @Resource
    private IdMembershipDao idMembershipDao;

	@Override
	public int insert(IdMembership entity) {
		return idMembershipDao.insert(entity);
	}

	@Override
	public int delete(Long id) {
		return idMembershipDao.deleteById(id);
	}

	@Override
	public int update(IdMembership entity) {
		return idMembershipDao.update(entity);
	}

	
	@Override
	public List<IdMembership> list(Map<String, Object> params) {
		return idMembershipDao.listSelective(params);
	}

	@Override
	public IdMembership findById(Long id) {
		return idMembershipDao.findById(id);
	}
}