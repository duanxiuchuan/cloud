package com.erongdu.server.activiti.model.request;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AttachmentRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private MultipartFile[] file;
	private String taskId;
	private String processDefinitionId;
	private String processInstanceId;
	

}
