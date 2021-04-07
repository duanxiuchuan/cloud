package com.erongdu.server.activiti.service;

import java.util.List;
import java.util.Map;

import com.erongdu.common.core.entity.activiti.ProcdefForm;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-21
 */
public interface ProcdefFormService{

	/**
	 * 插入数据
	 * @param entity
	 * @return 
	 */
	int insert(ProcdefForm entity);
		
	/**
	 * 删除数据
	 * @param id
	 * @return 
	 */
	int delete(Long id);
		
	/**
	 * 更新数据
	 * @param entity
	 * @return 
	 */
	int update(ProcdefForm entity);
	 
	/**
	 * 查询数据列表
	 * @param model
	 * @return
	 */
	List<ProcdefForm> list(Map<String, Object> params);

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	ProcdefForm findById(Long id);

}