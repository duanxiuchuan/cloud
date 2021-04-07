package com.erongdu.server.activiti.model.response;

import com.erongdu.common.core.enums.SuspensionState;

import lombok.Data;

@Data
public class ProcessDefinitionResponse {

	private String id;//流程定义id
	protected String name;//流程定义名称
	protected String description;//描述信息
	protected String key;//流程定义key
	protected int version;//版本号
	protected String category;//分类
	protected String deploymentId;//流程部署id
	protected String resourceName;//资源名称（bpmn）
	protected String tenantId;
	protected String diagramResourceName;//资源名称（图片）
	protected int suspensionState;
	
	// 授权候选
	private String users;// 候选用户ids
	private String userNames;// 候选用户姓名
	private String groups;// 候选组ids
	private String groupNames;// 候选组名称
	
	
	/**
	 * 状态
	 * @return
	 */
	public String getSuspensionStateStr(){
		return SuspensionState.ofDesc(suspensionState);
	}

}
