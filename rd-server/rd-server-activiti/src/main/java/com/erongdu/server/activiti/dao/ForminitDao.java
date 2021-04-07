package com.erongdu.server.activiti.dao;

import java.util.Map;

import com.erongdu.common.core.base.BaseDao;
import com.erongdu.common.core.entity.activiti.Forminit;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-7
 */
public interface ForminitDao extends BaseDao<Forminit, Long>{
	
	int deleteByParams(Map<String, Object> params);

}