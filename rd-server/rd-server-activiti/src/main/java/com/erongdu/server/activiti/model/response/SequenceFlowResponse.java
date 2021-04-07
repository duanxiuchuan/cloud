package com.erongdu.server.activiti.model.response;

import lombok.Data;
/**
 * 顺序流信息
 * @author lh@erongdu.com
 * @since  2020年7月20日
 *
 */
@Data
public class SequenceFlowResponse {

	private String name;
	private String documentation;
	private String conditionExpression;
	private String sourceRef;
	private String targetRef;
	private String skipExpression;
	

}
