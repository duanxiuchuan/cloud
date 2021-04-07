package com.erongdu.server.activiti.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

import com.erongdu.common.core.entity.activiti.Formula;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-12
 */
public interface FormulaService{

	/**
	 * 插入数据
	 * @param entity
	 * @return 
	 */
	int insert(Formula entity);
	
	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	void insertBatch(List<Formula> list);
		
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
	int update(Formula entity);
	
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
	List<Formula> list(Map<String, Object> params);

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	Formula findById(Long id);
	
	/**
	 * 表单规则
	 * @param formKey
	 * @return
	 */
	List<Object> formula(Map<String, Object> params);
	
	/**
	 * 根据模板id删除
	 * @param formtplId
	 * @return
	 */
	int deleteByFormtplId(Long formtplId);

}