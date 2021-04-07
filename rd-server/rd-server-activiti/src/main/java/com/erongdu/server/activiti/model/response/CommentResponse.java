package com.erongdu.server.activiti.model.response;

import java.util.Date;

import lombok.Data;

/**
 * 批注信息
 * @author lh@erongdu.com
 * @since  2020年7月20日
 *
 */
@Data
public class CommentResponse {

	private String type;
	private String userId;//批注人
	private Date time;//批注时间
	private String taskId;//任务id
	private String message;//批注内容
	private String processInstanceId;
	private String action;
	private String fullMessage;
	private String assigneeName;//办理人

}
