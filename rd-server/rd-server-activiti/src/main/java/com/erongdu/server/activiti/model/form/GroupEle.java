package com.erongdu.server.activiti.model.form;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupEle {

	private String label;//分组名称
	
	private List<ColumnEle> column;// 分组内表单元素
	
	
	
}
