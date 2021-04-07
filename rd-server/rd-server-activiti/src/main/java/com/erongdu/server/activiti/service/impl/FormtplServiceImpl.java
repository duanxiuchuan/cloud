package com.erongdu.server.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erongdu.common.core.entity.activiti.Forminit;
import com.erongdu.common.core.entity.activiti.Formtpl;
import com.erongdu.common.core.entity.activiti.Formula;
import com.erongdu.common.core.enums.SuspensionState;
import com.erongdu.server.activiti.dao.FormtplDao;
import com.erongdu.server.activiti.model.form.ColumnEle;
import com.erongdu.server.activiti.model.form.FormEle;
import com.erongdu.server.activiti.model.form.GroupEle;
import com.erongdu.common.core.entity.DictResponse;
import com.erongdu.server.activiti.service.ForminitService;
import com.erongdu.server.activiti.service.ForminstService;
import com.erongdu.server.activiti.service.FormtplService;
import com.erongdu.server.activiti.service.FormulaService;
import com.erongdu.server.activiti.util.PageableUtil;

/**
 * 表单模板管理
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-21
 */

@Service("formtplService")
public class FormtplServiceImpl implements FormtplService {

	@Resource
	private FormtplDao formtplDao;
	@Resource
	private ForminitService forminitService;
	@Resource
	private ForminstService forminstService;
	@Resource
	private FormulaService formulaService;

	@Override
	public int save(Formtpl entity) {
				
		// 表单是否分组
		if (StringUtils.isNotEmpty(entity.getFormContent())) {
			JSONObject content = JSONObject.parseObject(entity.getFormContent());
			entity.setGrouped(content.containsKey("group"));
		} else {
			entity.setGrouped(false);
		}

		if (entity.getId() == null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("formKey", entity.getFormKey());
			if(formtplDao.countSelective(params) > 0) {
				throw new RuntimeException("表单key已被使用");
			}
			entity.setVersion(1);
			entity.setSuspensionState(SuspensionState.ACTIVE.getCode());
			entity.setCreateTime(new Date());
			return formtplDao.insert(entity);
		} else {
			return edit(entity);
		}
	}

	@Override
	public int edit(Formtpl form) {
		Formtpl dbForm = formtplDao.findById(form.getId());
		if (form.getId() == null || dbForm.getId() == null) {
			throw new RuntimeException("请先新建表单");
		}
		
		// 表单内容
		String formContent = form.getFormContent();
		FormEle formEle = null;
		if(StringUtils.isNotEmpty(formContent)) {
			formEle = FormEle.of(formContent);
			// 表单是否分组
			form.setGrouped(formEle.isGroup());
		}
		
		int result = 0;
		if(StringUtils.isEmpty(dbForm.getFormContent()) || dbForm.getFormContent().equals(formContent)) {
			Formtpl entity = new Formtpl();
			entity.setId(form.getId());
			entity.setFormName(form.getFormName());
			entity.setGrouped(form.getGrouped());
			entity.setFormContent(formContent);
			entity.setLinkedFlag(form.getLinkedFlag());
			entity.setAssociateFlag(form.getAssociateFlag());
			entity.setLatestUpdateTime(new Date());
			result = formtplDao.update(entity);			
		}else {
			String formKey = dbForm.getFormKey();
			
			form.setId(null);			
			form.setVersion(formtplDao.findMaxVersionByKey(formKey)+1);
			form.setSuspensionState(SuspensionState.ACTIVE.getCode());
			form.setCreateTime(new Date());
			form.setFormKey(dbForm.getFormKey());
			form.setFormName(dbForm.getFormName());
			result = formtplDao.insert(form);
			// 拷贝表单算式
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("formtplId", dbForm.getId());
			List<Formula> fmlist = formulaService.list(params);
			
			long formtplId = form.getId();			
			if(fmlist != null && !fmlist.isEmpty()) {
				for (Formula formula : fmlist) {
					formula.setId(null);
					formula.setFormtplId(form.getId());
				}
				formulaService.insertBatch(fmlist);
			}
			
			if(formEle == null) {
				return result;
			}
			// 表单预设
			List<ColumnEle> columns = new ArrayList<ColumnEle>();
			
			if(formEle.isGroup()) {
				List<GroupEle> groups = formEle.getGroup();
				for (GroupEle group : groups) {
					columns.addAll(group.getColumn());
				}
			}else {
				columns.addAll(formEle.getColumn());
			}
			
			List<Forminit> fis = new ArrayList<>();
			for (ColumnEle ce : columns) {
				if(StringUtils.isNotEmpty(ce.getEleValue())) {
					Forminit fi = new Forminit(formKey, ce.getLabel(), ce.getCfgName(), ce.getEleValue(), formtplId);
					fis.add(fi);
				}
			}
			if(fis != null && !fis.isEmpty()) {
				forminitService.insertBatch(fis);
			}
			
		}
		return result;
	}
	
	@Override
	public int copy(Formtpl form) {
		int result = 0;
		Long sourceId = form.getSourceId();
		Formtpl source = formtplDao.findById(sourceId);
		if(source == null) {
			throw new RuntimeException("请选择复制表单");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formKey", form.getFormKey());
		if(formtplDao.countSelective(params) > 0) {
			throw new RuntimeException("表单key已被使用");
		}
		form.setLinkedFlag(source.getLinkedFlag());
		form.setAssociateFlag(source.getAssociateFlag());
		form.setGrouped(source.getGrouped());
		form.setFormContent(source.getFormContent());
		form.setCreateTime(new Date());
		form.setVersion(1);
		form.setSuspensionState(SuspensionState.ACTIVE.getCode());
		result = formtplDao.insert(form);
		// 拷贝表单算式
		params.clear();
		params.put("formtplId", sourceId);
		List<Formula> fmlist = formulaService.list(params);
		long formtplId = form.getId();
		
		if(fmlist != null && !fmlist.isEmpty()) {
			for (Formula formula : fmlist) {
				formula.setId(null);
				formula.setFormtplId(formtplId);
			}
			formulaService.insertBatch(fmlist);
		}
		
		List<Forminit> fis = forminitService.list(params);		
		if(fis != null && !fis.isEmpty()) {
			for (Forminit init : fis) {
				init.setId(null);
				init.setFormtplId(formtplId);
			}
			forminitService.insertBatch(fis);
		}		
		return result;
	}

	@Override
	public int delete(Long id) {
		Formtpl form = formtplDao.findById(id);
		if(form == null) {
			throw new RuntimeException("请选择表单");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formtplId", form.getId());
		if(forminstService.count(params) > 0) {
			throw new RuntimeException("已产生流程实例，请勿删除");
		}
		// 删除表单预设
		forminitService.deleteByFormtplId(id);
		// 删除表单算式
		formulaService.deleteByFormtplId(id);
		// 删除表单模板
		return formtplDao.deleteById(id);
	}

	@Override
	public Map<String, Object> page(Map<String, Object> params, Pageable pageable) {
		IPage<Formtpl> viewPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());	
		IPage<Formtpl> result = formtplDao.pageSelective(viewPage, params);
		for (Formtpl form : result.getRecords()) {
			form.setVariables(forminitService.init(form.getId(), null));
		}
		return PageableUtil.toPage(result.getRecords(), result.getTotal());
	}

	@Override
	public List<Formtpl> list(Map<String, Object> params) {
		return formtplDao.listSelective(params);
	}

	@Override
	public Formtpl findById(Long id) {
		return formtplDao.findById(id);
	}

	@Override
	public Formtpl findByProcessDefinitionId(String processDefinitionId) {
		return formtplDao.findByProcessDefinitionId(processDefinitionId);
	}

	@Override
	public List<DictResponse> listDict(Map<String, Object> params) {
		return formtplDao.listDict(params);
	}

	@Override
	public Formtpl findByFormKey(String formKey) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formKey", formKey);
		params.put("latestVersion", "1");		
		return formtplDao.findSelective(params);
	}
	
	@Override
	public int updateState(Formtpl formtpl) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", formtpl.getId());
		params.put("suspensionState", formtpl.getSuspensionState());
		return formtplDao.updateSelective(params);
	}
}