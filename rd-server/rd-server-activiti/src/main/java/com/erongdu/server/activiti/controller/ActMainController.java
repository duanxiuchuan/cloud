package com.erongdu.server.activiti.controller;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.base.BaseController;
import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.common.tool.service.ConfigService;
import com.erongdu.server.activiti.service.ForminstService;

@RestController
@RequestMapping("/actmain")
public class ActMainController extends BaseController{
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private ForminstService forminstService;
	@Autowired
	private ConfigService configService;
	
	@GetMapping
	public Object index() {
		Map<String, Object> data = new HashMap<String, Object>();
		
    	String username = CommonUtil.getCurrentUsername();
    	
    	// 待办任务数
    	TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned(username);
    	data.put("undoTaskCount", query.count());  
    	
    	// 我的请求数
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("username", username);
    	data.put("myInstCount", forminstService.count(params));  
    	
		return success(data);		
	}
	
	@GetMapping("/config/{code}")
	public Object config(@PathVariable String code) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("value", configService.findByCode(code));
    	return result;
	}

}
