package com.erongdu.server.activiti.dao;

import java.util.Map;

import com.erongdu.common.core.base.BaseDao;
import com.erongdu.common.core.entity.activiti.Formula;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-12
 */
public interface FormulaDao extends BaseDao<Formula, Long>{
	
	
	int deleteByParams(Map<String, Object> params);

}