package com.erongdu.server.activiti.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

import com.erongdu.common.core.entity.activiti.Forminit;
import com.erongdu.server.activiti.model.form.ColumnEle;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-7
 */
public interface ForminitService{

	/**
	 * 插入数据
	 * @param entity
	 * @return 
	 */
	int insert(Forminit entity);
	
	/**
	 * 批量插入 
	 * @param list
	 * @return
	 */
	void insertBatch(List<Forminit> list);
		
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
	int update(Forminit entity);
	
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
	List<Forminit> list(Map<String, Object> params);

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	Forminit findById(Long id);
	
	/**
	 * 初始化
	 * @param formtplId 表单模板id
	 * @param variables
	 * @param columns
	 * @return
	 */
	String init(Long formtplId, String variables, List<ColumnEle> columns);
	
	/**
	 * 表单数据初始化
	 * @param formtplId	表单模板id
	 * @param map
	 * @return
	 */
	String init(Long formtplId, Map<String, Object> map);
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	int countSelective(Map<String, Object> map);
	
	/**
	 * 根据模板id删除
	 * @param formtplId
	 * @return
	 */
	int deleteByFormtplId(Long formtplId);

}