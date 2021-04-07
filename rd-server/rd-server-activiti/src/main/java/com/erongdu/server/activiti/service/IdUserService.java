package com.erongdu.server.activiti.service;

import java.util.List;
import java.util.Map;

import com.erongdu.common.core.entity.activiti.IdUser;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-28
 */
public interface IdUserService{
	 
	/**
	 * 查询数据列表
	 * @param model
	 * @return
	 */
	List<IdUser> list(Map<String, Object> params);

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	IdUser findById(String id);

}