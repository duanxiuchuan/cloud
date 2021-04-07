package com.erongdu.server.activiti.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.erongdu.server.activiti.model.request.DefinitionStartableRequest;
import com.erongdu.server.activiti.model.request.DelegateTaskRequest;
import com.erongdu.server.activiti.model.request.DeploymentRequest;
import com.erongdu.server.activiti.model.request.ProcessStartRequest;
import com.erongdu.server.activiti.model.request.ProcessStopRequest;
import com.erongdu.server.activiti.model.request.TaskCompleteRequest;
import com.erongdu.server.activiti.model.request.TaskRequest;
import com.erongdu.server.activiti.model.response.HistoricActivityResponse;

public interface ActivitiService {
	
	
	/**
	 * 部署流程
	 * @param model
	 * @param in
	 * @return
	 */
	String deploy(DeploymentRequest model, InputStream in);
	
	/**
	 * 查询流程部署列表
	 * @param name
	 */
	Map<String, Object> queryDeployments(Map<String, Object> params, Pageable page);
	
	/**
	 * 查询流程定义
	 * @param params
	 * @return
	 */
	Map<String, Object> queryProcessDefinitions(Map<String, Object> params, Pageable page);
	
	/**
	 * 查询可新建的流程
	 * @param params
	 * @return
	 */
	Map<String, Object> queryProcessList(Map<String, Object> params, Pageable page);
	
	/**
	 * 准备启动流程
	 * @param model
	 * @return
	 */
	Map<String, Object> prepareProcess(ProcessStartRequest model);
	
	/**
	 * 启动流程
	 * @param model
	 * @return
	 */
	String startProcess(ProcessStartRequest model);
	
	
	/**
	 * 待办任务列表
	 * @param page
	 * @return
	 */
	Map<String, Object> queryTasks(Map<String, Object> params, Pageable page);
	
	/**
	 * 已办任务列表
	 * @param pageable
	 * @return
	 */
	Map<String, Object> queryHisTasks(Map<String, Object> params,Pageable pageable);
	
	/**
	 * 查询待办任务详细信息
	 * @param taskId	任务id
	 * @return
	 */
	Map<String, Object> queryTodoTaskById(String taskId);
	
	/**
	 * 查询历史任务详细信息
	 * @param taskId	任务id
	 * @return
	 */
	Map<String, Object> queryHisTaskById(String taskId);
	
	/**
	 * 表单实例详情
	 * @param formId	表单id
	 * @return
	 */
	Map<String, Object> queryForminst(Long formId);
	
	/**
	 * 完成任务
	 * @param task
	 */
	void doTask(TaskCompleteRequest task);
	
	/**
	 * 任务委派
	 * @param delegateTask
	 */
	void delegateTask(DelegateTaskRequest delegateTask);
	
	/**
	 * 任务转办
	 * @param model
	 */
	void transferTask(TaskRequest model);
	
	/**
	 * 任务抄送
	 * @param model
	 */
	void ccTask(TaskRequest model);
	
	/**
	 * 设置流程定义启动候选人/候选组
	 * @param startable
	 */
	void saveStartableUsers(DefinitionStartableRequest startable);
	
	/**
	 * 审批历史记录
	 * @param historicActivity
	 * @return
	 */
	List<HistoricActivityResponse> queryHisActivities(HistoricActivityResponse historicActivity);
	
	
	/**
	 * 强制终止流程
	 * @param model
	 */
	void stopProcess(ProcessStopRequest model);
}  