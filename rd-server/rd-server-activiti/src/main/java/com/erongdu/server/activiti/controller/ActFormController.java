package com.erongdu.server.activiti.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.base.BaseController;
import com.erongdu.common.core.entity.activiti.Forminst;
import com.erongdu.common.core.entity.activiti.Formtpl;
import com.erongdu.common.core.utils.BindingResultUtils;
import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.server.activiti.service.ForminstService;
import com.erongdu.server.activiti.service.FormtplService;
/**
 * 表单模板管理
 * @author lh@erongdu.com
 * @since  2020年7月21日
 *
 */
@RestController
@RequestMapping(value = "/actform")
public class ActFormController extends BaseController{

	@Autowired
	private FormtplService formtplService;
	@Autowired
	private ForminstService forminstService;
	
	/**
	 * 表单模板列表
	 * @param params
	 * @param pageable
	 * @return
	 */
	@GetMapping("/tpl/list")
	public Object tplList(@RequestParam Map<String, Object> params, Pageable pageable) {
		Map<String, Object> body = formtplService.page(params, pageable);
		return success(body);
	}
	
	/**
	 * 表单模板字典列表
	 * @param params
	 * @return
	 */
	@GetMapping("/tpl/listDict")
	public Object tplList(@RequestParam Map<String, Object> params) {
		return success(formtplService.listDict(params));
	}
	
	/**
	 * 保存表单模板
	 * @param form
	 * @return
	 */
	@PostMapping("/tpl/save")
	public Object tplSave(@RequestBody @Validated Formtpl form, BindingResult result) {
		if(result.hasErrors()) {
			return fail(BindingResultUtils.getResult(result));
		}
		formtplService.save(form);
		return success(); 
	}
	
	/**
	 * 编辑表单模板
	 * @param form
	 * @return
	 */
	@PostMapping("/tpl/edit")
	public Object tplEdit(@RequestBody @Validated Formtpl form, BindingResult result) {
		if(result.hasErrors()) {
			return fail(BindingResultUtils.getResult(result));
		}
		formtplService.edit(form);
		return success(); 
	}
	
	/**
	 * 复制表单模板
	 * @param form
	 * @return
	 */
	@PostMapping("/tpl/copy")
	public Object tplCopy(@RequestBody @Validated Formtpl form, BindingResult result) {
		if(result.hasErrors()) {
			return fail(BindingResultUtils.getResult(result));
		}
		formtplService.copy(form);
		return success(); 
	}
	
	/**
	 * 删除表单模板
	 * @param id
	 * @return
	 */
	@DeleteMapping("/tpl/delete/{id}")
	public Object tplDel(@PathVariable Long id) {
		formtplService.delete(id);
		return success(); 
	}
	
	/**
	 * 表单内容查询
	 * @param params
	 * @return
	 */
	@GetMapping( value = "/tpl/{id}", produces="text/plain;charset=UTF-8")
	public Object tplList(@PathVariable("id") Long id) {
		Formtpl form = formtplService.findById(id);
		return success(form == null ? null : form.getFormContent());
	}
	
	/**
	 * 更新表单模板状态
	 * @param formtpl
	 * @return
	 */
	@PutMapping("/tpl/updateState")
	public Object updateState(@RequestBody Formtpl formtpl) {
		formtplService.updateState(formtpl);
		return success();
	}
	
	/**
	 * 所有请求-列表
	 * @param params
	 * @param pageable
	 * @return
	 */
	@GetMapping("/inst/list")
	public Object instList(@RequestParam Map<String, Object> params, Pageable pageable) {
		Map<String, Object> body = forminstService.page(params, pageable);
		return success(body);
	}
	
	/**
	 * 我的请求-列表
	 * @param params
	 * @param pageable
	 * @return
	 */
	@GetMapping("/inst/mylist")
	public Object myinstlist(@RequestParam Map<String, Object> params, Pageable pageable) {
		params.put("username", CommonUtil.getCurrentUsername());
		Map<String, Object> body = forminstService.page(params, pageable);
		return success(body);
	}
	
	/**
	 * 关联流程查询
	 * @param params
	 * @param pageable
	 * @return
	 */
	@GetMapping("/inst/linked")
	public Object instlinked(@RequestParam Map<String, Object> params) {
		params.put("username", CommonUtil.getCurrentUsername());
		params.put("endTimeNotnull", "1");
		return success(forminstService.search(params));
	}
	
	/**
	 * 表单数据
	 * @param params
	 * @param pageable
	 * @return
	 */
	@GetMapping("/inst/form")
	public Object forminst(String processInstanceId) {
		Map<String, Object> result = new HashMap<String, Object>();
    	// 获取表单实例
    	Forminst form = forminstService.findByInstanceId(processInstanceId);
    	result.put("form", form); 
		return success(result);
	}

	/**
	 * 删除表单实例
	 * @param id
	 * @return
	 */
	@DeleteMapping("/inst/delete/{id}")
	public Object instDel(@PathVariable Long id) {
		forminstService.delete(id);
		return success(); 
	}
	
	
}
