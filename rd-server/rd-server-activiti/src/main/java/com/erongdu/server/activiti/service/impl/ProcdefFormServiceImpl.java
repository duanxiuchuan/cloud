package com.erongdu.server.activiti.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.erongdu.common.core.entity.activiti.ProcdefForm;
import com.erongdu.server.activiti.dao.ProcdefFormDao;
import com.erongdu.server.activiti.service.ProcdefFormService;

/**
 * 流程定义和表单模板关联
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-21
 */
 
@Service("procdefFormService") 
public class ProcdefFormServiceImpl implements ProcdefFormService{
	
    @Resource
    private ProcdefFormDao procdefFormDao;

	@Override
	public int insert(ProcdefForm entity) {
		return procdefFormDao.insert(entity);
	}

	@Override
	public int delete(Long id) {
		return procdefFormDao.deleteById(id);
	}

	@Override
	public int update(ProcdefForm entity) {
		return procdefFormDao.update(entity);
	}

	@Override
	public List<ProcdefForm> list(Map<String, Object> params) {
		return procdefFormDao.listSelective(params);
	}

	@Override
	public ProcdefForm findById(Long id) {
		return procdefFormDao.findById(id);
	}
}