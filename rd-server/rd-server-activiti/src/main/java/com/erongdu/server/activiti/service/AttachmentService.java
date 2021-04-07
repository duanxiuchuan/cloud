package com.erongdu.server.activiti.service;

import java.util.List;
import java.util.Map;

import com.erongdu.common.core.entity.activiti.Attachment;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-6
 */
public interface AttachmentService{

/**
	 * 插入数据
	 * @param entity
	 * @return 
	 */
	int insert(Attachment entity);
		
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
	int update(Attachment entity);
	
	/**
	 * 查询数据列表
	 * @param model
	 * @return
	 */
	List<Attachment> list(Map<String, Object> params);

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	Attachment findById(Long id);

	/**
	 * 批量添加
	 * @param list
	 */
	void insertBatch(List<Attachment> list);
	
}