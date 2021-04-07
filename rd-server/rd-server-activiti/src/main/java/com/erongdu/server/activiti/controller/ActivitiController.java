package com.erongdu.server.activiti.controller;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erongdu.common.core.base.BaseController;
import com.erongdu.server.activiti.model.request.DefinitionStartableRequest;
import com.erongdu.server.activiti.model.request.DelegateTaskRequest;
import com.erongdu.server.activiti.model.request.DeploymentRequest;
import com.erongdu.server.activiti.model.request.ProcessStartRequest;
import com.erongdu.server.activiti.model.request.ProcessStopRequest;
import com.erongdu.server.activiti.model.request.TaskCompleteRequest;
import com.erongdu.server.activiti.model.request.TaskRequest;
import com.erongdu.server.activiti.model.response.HistoricActivityResponse;
import com.erongdu.server.activiti.service.ActivitiService;

@RestController
@RequestMapping(value = "/activiti")
public class ActivitiController extends BaseController{

	private static final Logger log = LoggerFactory.getLogger(ActivitiController.class);
	
	@Autowired
	private ActivitiService activitiService; 
	
	/**
	 * 流程部署
	 * @param file
	 * @return
	 */
	@PostMapping(value = "/deploy")  
	public Object deploy(DeploymentRequest model, @RequestParam("file") MultipartFile file) {  
		
		try {
			if(file == null || file.getInputStream() == null) {
				return fail("请上传部署文件");
			}
		} catch (IOException e1) {
			return fail("请上传部署文件");
		}
		
		try {  
			if(model == null) {
				model = new DeploymentRequest();
			}
			String fileName = file.getOriginalFilename();
			if(StringUtils.isEmpty(model.getKey())) {
				model.setKey(fileName);
			}
			model.setName(fileName);
			model.setSourceName(fileName);
			
			activitiService.deploy(model, file.getInputStream());  
		} catch (IOException e) {  
			log.error(e.getMessage(), e);
			return fail("流程部署失败");
		}  
		return success();
	}  
	
	
	
	/**
	 * 流程定义列表-用于定义流程
	 * @param model
	 * @return
	 */
	@GetMapping("/processDefinitions")
	public Object processDefinitions(@RequestParam Map<String, Object> params, Pageable pageable) {
		Map<String, Object> data = activitiService.queryProcessDefinitions(params, pageable);
		return success(data);
	}
	
	/**
	 * 流程定义列表- 用于流程发起
	 * @param model
	 * @return
	 */
	@GetMapping("/processList")
	public Object processList(@RequestParam Map<String, Object> params, Pageable pageable) {
		Map<String, Object> data = activitiService.queryProcessList(params, pageable);
		return success(data);
	}
	
	/**
	 * 流程发起准备
	 * @param model
	 * @return
	 */
	@GetMapping("/prepare")
	public Object prepareProcess(@Validated ProcessStartRequest model) {
		Map<String, Object> data = activitiService.prepareProcess(model);
		return success(data);
	}
	
	/**
	 * 流程发起
	 * @return
	 */
	@PostMapping(value = "/start")
	public Object start(@Validated ProcessStartRequest model) {
		activitiService.startProcess(model);
		return success();
	}
	
	/**
	 * 任务委托，委托任务可添加批注
	 * @return
	 */
	@PostMapping(value = "/delegateTask")
	public Object delegateTask(@Validated @RequestBody DelegateTaskRequest task) {
		activitiService.delegateTask(task);
		return success();
	}
	
	/**
	 * 任务转办，转办任务可全权处理
	 * @return
	 */
	@PostMapping(value = "/transferTask")
	public Object transferTask(@Validated @RequestBody TaskRequest model) {
		activitiService.transferTask(model);
		return success();
	}
	
	/**
	 * 任务抄送，抄送任务可添加批注
	 * @return
	 */
	@PostMapping(value = "/ccTask")
	public Object ccTask(@Validated @RequestBody TaskRequest model) {
		activitiService.ccTask(model);
		return success();
	}
	
	/**
	 * 处理任务
	 * @return
	 */
	@PostMapping(value = "/doTask")
	public Object doTask(@Validated TaskCompleteRequest task) {
		activitiService.doTask(task); 
		return success();
	}
	
	/**
	 * 待办任务
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/tasks")
	public Object tasks(@RequestParam Map<String, Object> params, Pageable page) {
		return success(activitiService.queryTasks(params, page));
	}
	
	/**
	 * 待办详情
	 * @param taskId
	 * @return
	 */
	@GetMapping("/taskDetail")
	public Object taskDetail(String taskId) {
		return success(activitiService.queryTodoTaskById(taskId));
	}
	
	
	/**
	 * 已办任务
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/hisTasks")
	public Object hisTasks(@RequestParam Map<String, Object> params, Pageable page) {
		return success(activitiService.queryHisTasks(params, page));
	}
	
	
	/**
	 * 审批记录
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/hisActivities")
	public Object hisActivities(HistoricActivityResponse historicActivity) {
		return success(activitiService.queryHisActivities(historicActivity));
	}
	
	/**
	 * 已办详情
	 * @param taskId
	 * @return
	 */
	@GetMapping("/hisTaskDetail")
	public Object hisTaskDetail(String taskId) {
		return success(activitiService.queryHisTaskById(taskId));
	}
	
	/**
	 * 请求详情
	 * @param formId
	 * @return
	 */
	@GetMapping("/forminst")
	public Object forminst(Long formId) {
		return success(activitiService.queryForminst(formId));
	}
	
	/**
	 * 设置流程定义启动候选人/候选组
	 * @return
	 */
	@PostMapping(value = "/saveStartableUsers")
	public Object saveStartableUsers(@Validated @RequestBody DefinitionStartableRequest startable) {
		activitiService.saveStartableUsers(startable);
		return success();
	}
	
	
	/**
	 * 强制终止流程
	 * @return
	 */
	@PostMapping(value = "/stopProcess")
	public Object stopProcess(@Validated @RequestBody ProcessStopRequest model) {
		activitiService.stopProcess(model);
		return success();
	}
	
	
	
	
}