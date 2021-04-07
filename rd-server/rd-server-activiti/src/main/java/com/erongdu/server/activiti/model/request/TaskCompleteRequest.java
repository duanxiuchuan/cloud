package com.erongdu.server.activiti.model.request;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * 任务处理
 * @author lh@erongdu.com
 * @since  2020年7月20日
 *
 */
@Data
public class TaskCompleteRequest {

	@NotEmpty
	private String taskId;//任务id
	private String outcome;	//出口连线
	private String comment;//批注信息
	private String formKey;// 表单key
	private String formContent;	//表单内容
	private Boolean saveSubmit = false;//保存并提交
	private String[] targetRef;// 下一步任务key
	private String[] targetAssignee;// 下一步任务处理人 
	private String variablesStr;
	// 表单参数值
	private Map<String, Object> variables = new HashMap<String, Object>();
	private MultipartFile[] file ;//附件信息
	
	
}
