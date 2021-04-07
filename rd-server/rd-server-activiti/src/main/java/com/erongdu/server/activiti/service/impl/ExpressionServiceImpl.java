package com.erongdu.server.activiti.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erongdu.common.core.entity.activiti.Expression;
import com.erongdu.server.activiti.dao.ExpressionDao;
import com.erongdu.server.activiti.service.ExpressionService;
import com.erongdu.server.activiti.util.PageableUtil;

/**
 * 条件表达式
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-5
 */
 
@Service("expressionService") 
public class ExpressionServiceImpl implements ExpressionService{
	
    @Resource
    private ExpressionDao expressionDao;

	@Override
	public int save(Expression entity) {
		if(entity.getId() == null) {
			entity.setCreateTime(new Date());
			entity.setDeleteFlag(false);
			return expressionDao.insert(entity);
		}else {
			entity.setLastUpdateTime(new Date());
			return expressionDao.update(entity);
		}
	}

	@Override
	public int delete(Long id) {
		return expressionDao.deleteById(id);
	}

	@Override
	public int update(Expression entity) {
		entity.setLastUpdateTime(new Date());
		return expressionDao.update(entity);
	}

	@Override
	public Map<String,Object> page(Map<String, Object> params, Pageable pageable) {
		IPage<Expression> viewPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());	
    	IPage<Expression> result = expressionDao.pageSelective(viewPage, params);
        return PageableUtil.toPage(result.getRecords(), result.getTotal());
	}
	
	@Override
	public List<Expression> list(Map<String, Object> params) {
		return expressionDao.listSelective(params);
	}

	@Override
	public Expression findById(Long id) {
		return expressionDao.findById(id);
	}
}