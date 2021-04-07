package com.erongdu.server.activiti.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

import com.erongdu.common.core.entity.activiti.Formtpl;
import com.erongdu.common.core.entity.DictResponse;

/**
 * 表单模板
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-21
 */
public interface FormtplService{

	/**
	 * 插入数据
	 * @param entity
	 * @return 
	 */
	int save(Formtpl entity);
	
	/**
	 * 编辑表单
	 * @param form
	 * @return
	 */
	int edit(Formtpl form);
	
	/**
	 * 复制表单
	 * @param form
	 * @return
	 */
	int copy(Formtpl form);
		
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
	List<Formtpl> list(Map<String, Object> params);

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	Formtpl findById(Long id);
	
	/**
	 * 根据流程定义id获取表单模板
	 * @param processDefinitionId
	 * @return
	 */
	Formtpl findByProcessDefinitionId(String processDefinitionId);
	/**
	 * 根据formKey获取表单模板
	 * @param formKey
	 * @return
	 */
	Formtpl findByFormKey(String formKey);
	
	
	/**
	 * 返回form字典列表（value:formKey, label:formName）
	 * @param params
	 * @return
	 */
	List<DictResponse> listDict(Map<String, Object> params);
	
	/**
	 * 更新状态
	 * @param formtpl
	 * @return
	 */
	int updateState(Formtpl formtpl);

}