package com.erongdu.server.activiti.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.erongdu.server.activiti.model.response.ProcessDefinitionResponse;

public interface ActRepositoryService {
	
	/**
	 * 可新建的流程定义列表
	 * @param params
	 * @param pageable
	 * @return
	 */
	Map<String, Object> queryProcessList(Map<String, Object> params, Pageable pageable);
	
	
	/**
	 * 查询流程定义候选用户和候选组
	 * @param id
	 * @param model
	 */
	void queryProcessCandicate(String id, ProcessDefinitionResponse model);

}
