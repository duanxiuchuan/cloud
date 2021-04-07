package com.erongdu.server.activiti.service;

import java.util.List;
import java.util.Map;

import com.erongdu.common.core.entity.activiti.IdGroup;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-28
 */
public interface IdGroupService{

/**
	 * 插入数据
	 * @param entity
	 * @return 
	 */
	int insert(IdGroup entity);
		
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
	int update(IdGroup entity);
	 
	/**
	 * 查询数据列表
	 * @param model
	 * @return
	 */
	List<IdGroup> list(Map<String, Object> params);

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	IdGroup findById(Long id);

}