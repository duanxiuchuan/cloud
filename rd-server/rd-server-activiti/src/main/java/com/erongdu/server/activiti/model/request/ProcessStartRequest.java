package com.erongdu.server.activiti.model.request;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * 启动流程信息
 * @author lh@erongdu.com
 * @since  2020年7月20日
 *
 */
@Data
public class ProcessStartRequest {
	
	@NotEmpty
	private String processDefinitionId;//流程定义id
	private String assignee;	// 任务执行人
	private String formKey;// 表单key
	private Boolean submit = Boolean.FALSE;//启动流程并提交
	private String linkedId;		/* 关联流程实例id，多个用英文逗号分隔 */
	private String linkedName;		/* 关联流程实例名称，多个用英文逗号分隔 */
	//private String nextAssignee;// 下一个任务执行人
	private String variablesStr;
	private Map<String, Object> variables = new HashMap<>();
	private MultipartFile[] file ;//附件信息
	private String associate;// 关联人员
	private Long forminstId;	//表单实例id

}
