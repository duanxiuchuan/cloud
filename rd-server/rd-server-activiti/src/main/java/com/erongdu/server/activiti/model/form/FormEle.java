package com.erongdu.server.activiti.model.form;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

import lombok.Data;

@Data
public class FormEle {
	
	private List<ColumnEle> column;	// 表单元素
	private List<GroupEle> group;	// 表单分组
	
	/**
	 * json串 转 表单对象
	 * @param formJson
	 * @return
	 */
	public static FormEle of(String formJson) {
		if(StringUtils.isEmpty(formJson)) {
			return null;
		}
		return JSON.parseObject(formJson, FormEle.class);
	}
	
	/**
	 * 获取控件值
	 * @param label
	 * @return
	 */
	public String getColumnValue(String label) {
		for (ColumnEle ce : column) {
			if(ce.getLabel().equalsIgnoreCase(label)) {
				return ce.getValue();
			}
		}
		return null;
	}
	
	/**
	 * 表单是否分组
	 * @return
	 */
	public boolean isGroup() {
		return group != null && group.size() > 0;
	}
	
	
	public static void main(String[] args) {
		String formStr = "{\"column\":[{\"type\":\"input\",\"label\":\"标题\",\"span\":24,\"display\":true,\"prop\":\"1595571402225_51314\",\"value\":\"请假单-张三-yyyy-MM-dd\"},{\"type\":\"input\",\"label\":\"申请人\",\"span\":24,\"display\":true,\"prop\":\"1595571240580_48521\",\"value\":\"申请人本人\"},{\"type\":\"daterange\",\"label\":\"请假时间\",\"span\":24,\"display\":true,\"format\":\"yyyy-MM-dd\",\"valueFormat\":\"yyyy-MM-dd\",\"prop\":\"1595571284904_29346\"},{\"type\":\"select\",\"label\":\"请假类型\",\"dicData\":[{\"label\":\"事假\",\"value\":0},{\"label\":\"年休假\",\"value\":1},{\"label\":\"产假\",\"value\":2},{\"label\":\"婚假\",\"value\":\"3\"},{\"label\":\"病假\",\"value\":\"4\"},{\"label\":\"其他\",\"value\":\"5\"}],\"cascaderItem\":[],\"span\":24,\"display\":true,\"prop\":\"1595571511325_73189\",\"props\":{\"label\":\"label\",\"value\":\"value\"}},{\"type\":\"input\",\"label\":\"请假事由\",\"span\":24,\"display\":true,\"prop\":\"1595571327265_50357\"},{\"type\":\"textarea\",\"label\":\"签字意见\",\"span\":24,\"display\":true,\"prop\":\"1595571609177_68272\"}],\"labelPosition\":\"left\",\"labelSuffix\":\"：\",\"labelWidth\":120,\"gutter\":0,\"menuBtn\":true,\"submitBtn\":true,\"submitText\":\"提交\",\"emptyBtn\":true,\"emptyText\":\"清空\",\"menuPosition\":\"center\"}";
		FormEle form = FormEle.of(formStr);
		List<ColumnEle> cols = form.getColumn();
		for (ColumnEle col : cols) {
			System.out.println(col.getLabel()+":"+ col.getValue());
		}
	}
	
}


