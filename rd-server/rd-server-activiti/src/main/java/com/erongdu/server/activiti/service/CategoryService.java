package com.erongdu.server.activiti.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

import com.erongdu.common.core.entity.activiti.Category;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-21
 */
public interface CategoryService{

	/**
	 * 保存数据
	 * @param entity
	 * @return 
	 */
	int save(Category entity);
		
	/**
	 * 删除数据
	 * @param id
	 * @return 
	 */
	int delete(Long id);
	
	/**
	 * 分页查询
	 * @param params 查询条件
	 * @param pageable 分页信息
	 * @return
	 */
	Map<String,Object> page(Map<String, Object> params, Pageable pageable);
	 
	/**
	 * 查询数据列表
	 * @param model
	 * @return
	 */
	List<Category> list(Map<String, Object> params);

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	Category findById(Long id);

}