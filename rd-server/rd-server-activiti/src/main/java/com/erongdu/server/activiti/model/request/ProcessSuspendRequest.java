package com.erongdu.server.activiti.model.request;

import lombok.Data;

/**
 * 流程状态变更
 * @author lh@erongdu.com
 * @since  2020年8月3日
 *
 */
@Data
public class ProcessSuspendRequest {
	
	private String id;// 流程定义id
	private int suspensionState;// 状态

}
