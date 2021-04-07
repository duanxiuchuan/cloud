package com.erongdu.server.activiti.dao;

import java.util.List;
import java.util.Map;

import com.erongdu.common.core.base.BaseDao;
import com.erongdu.common.core.entity.DictResponse;
import com.erongdu.common.core.entity.activiti.Formtpl;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-21
 */
public interface FormtplDao extends BaseDao<Formtpl, Long>{
	
	/**
	 * 根据流程定义id获取表单模板
	 * @param processDefinitionId
	 * @return
	 */
	Formtpl findByProcessDefinitionId(String processDefinitionId);
	
	/**
	 * 返回form字典列表（value:formKey, label:formName）
	 * @param params
	 * @return
	 */
	List<DictResponse> listDict(Map<String, Object> params);
	
	/**
	 * 根据formkey查询formName
	 * @param formKey
	 * @return
	 */
	String findFormNameByKey(String formKey);
	
	/**
	 * 取最高版本
	 * @param formKey
	 * @return
	 */
	int findMaxVersionByKey(String formKey);
	

}