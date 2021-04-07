package com.erongdu.server.activiti.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

import com.erongdu.common.core.entity.activiti.Forminst;
import com.erongdu.common.core.entity.DictResponse;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-21
 */
public interface ForminstService{

	/**
	 * 保存数据
	 * @param entity
	 * @return 
	 */
	int save(Forminst entity);
		
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
	List<Forminst> list(Map<String, Object> params);

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	Forminst findById(Long id);
	
	/**
	 * 根据流程实例id查询表单实例
	 * @param processInstanceId
	 * @return
	 */
	Forminst findByInstanceId(String processInstanceId);
	
	/**
	 * 更新节点名称
	 * @param params
	 */
	void updateNodeName(Map<String, Object> params);
	
	/**
	 * 更新节点名称
	 * @param processInstanceId	流程实例id
	 * @param executionId		执行id
	 */
	void updateNodeName(String processInstanceId, String executionId);
	
	 
	/**
	 * 查询数据列表数量
	 * @param params
	 * @return
	 */
	int count(Map<String, Object> params);
	
	/**
	 * 查询符合条件的流程实例
	 * @param params
	 * @return
	 */
	List<DictResponse> search(Map<String, Object> params);

}