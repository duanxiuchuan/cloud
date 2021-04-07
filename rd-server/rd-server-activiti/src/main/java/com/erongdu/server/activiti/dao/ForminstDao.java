package com.erongdu.server.activiti.dao;

import java.util.Map;

import com.erongdu.common.core.base.BaseDao;
import com.erongdu.common.core.entity.activiti.Forminst;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-21
 */
public interface ForminstDao extends BaseDao<Forminst, Long>{
	
	/**
	 * 根据流程实例id查询表单实例
	 * @param processInstanceId
	 * @return
	 */
	Forminst findByInstanceId(String processInstanceId);
	
	/**
	 * 更新节点名称
	 * @param params
	 * @return
	 */
	int updateNodeName(Map<String, Object> params);

}