package com.erongdu.server.activiti.service;

import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;

import com.erongdu.server.activiti.model.response.HistoricActivityResponse;

public interface ActHistoryService {
	
	/**
	 * 任务是否已归档
	 * @param processInstanceId
	 * @return
	 */
	boolean finished(String processInstanceId);
	
	
	/**
	 * 最新的用户任务
	 * @param executionId
	 * @return
	 */
	HistoricActivityInstance nextUserActivity(String executionId) ;
	
	/**
	 * 查询审批记录
	 * @param processInstanceId
	 * @param historicActivity
	 * @return
	 */
    List<HistoricActivityResponse> queryHisActivities(HistoricActivityResponse historicActivity);
    

}
