package com.erongdu.server.activiti.service.impl;

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
import com.erongdu.common.core.entity.system.SystemUser;
import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.server.activiti.dao.ForminitDao;
import com.erongdu.server.activiti.dao.FormtplDao;
import com.erongdu.server.activiti.model.form.ColumnEle;
import com.erongdu.server.activiti.service.ForminitService;
import com.erongdu.server.activiti.service.IUserService;
import com.erongdu.server.activiti.util.FreemarkerUtil;
import com.erongdu.server.activiti.util.PageableUtil;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-7
 */
 
@Service("forminitService") 
public class ForminitServiceImpl implements ForminitService{
	
    @Resource
    private ForminitDao forminitDao;
    @Resource
    private IUserService userService;
    @Resource
    private FormtplDao formDao;

	@Override
	public int insert(Forminit entity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formtplId", entity.getFormtplId());
		params.put("eleLabel", entity.getEleLabel());
		int count = forminitDao.countSelective(params);
		if(count > 0) {
			throw new RuntimeException(String.format("表单元素【%s】已经设置预设值！", entity.getEleLabel()));
		}		
		return forminitDao.insert(entity);
	}
	
	@Override
	public void insertBatch(List<Forminit> list) {
		forminitDao.insertBatch(list);
	}

	@Override
	public int delete(Long id) {
		return forminitDao.deleteById(id);
	}

	@Override
	public int update(Forminit entity) {
		return forminitDao.update(entity);
	}

	@Override
	public Map<String,Object> page(Map<String, Object> params, Pageable pageable) {
		IPage<Forminit> viewPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());	
    	IPage<Forminit> result = forminitDao.pageSelective(viewPage, params);
        return PageableUtil.toPage(result.getRecords(), result.getTotal());
	}
	
	@Override
	public List<Forminit> list(Map<String, Object> params) {
		return forminitDao.listSelective(params);
	}

	@Override
	public Forminit findById(Long id) {
		return forminitDao.findById(id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String init(Long formtplId, String variables, List<ColumnEle> columns) {
		if(columns == null || columns.isEmpty()) {
			return variables;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formtplId", formtplId);
		List<Forminit> list = forminitDao.listSelective(params);
		if(list == null || list.isEmpty()) {
			return variables;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(variables)) {
			map = JSONObject.parseObject(variables, Map.class);
		}
				
		boolean needInit = false;
		for (ColumnEle ce : columns) {
			if(StringUtils.isEmpty(ce.getLabel())) {
				continue;
			}
			for (Forminit fi : list) {		
				if(ce.getLabel().equals(fi.getEleLabel()) 
						&& !map.containsKey(fi.getEleLabel())) {//已填数据勿覆盖
					map.put(fi.getEleLabel(), fi.getEleValue());
					needInit = true;
				}
			}
		}			
		if(needInit) {
			return init(formtplId, map);
		}else {
			return variables;
		}		
	}
	
	@Override
	public String init(Long formtplId, Map<String, Object> map) {
		if(map == null || map.isEmpty()) {
			map = new HashMap<String, Object>();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("formtplId", formtplId);
			List<Forminit> list = forminitDao.listSelective(params);
			if(list == null || list.isEmpty()) {
				return null;
			}
			for (Forminit init : list) {
				map.put(init.getEleLabel(), init.getEleValue());
			}
		}
		if(map == null || map.isEmpty()) {
			return null;
		}		
		Map<String, Object> data = new HashMap<String, Object>();
		String username = CommonUtil.getCurrentUsername();
		SystemUser user = userService.findByName(username);
		data.put("user", user);	
		data.put("currentTime", new Date());
		
		return FreemarkerUtil.renderTemplate(JSONObject.toJSONString(map), data);
		
	}
	
	@Override
	public int countSelective(Map<String, Object> params) {
		return forminitDao.countSelective(params);
	}
	
	
	@Override
	public int deleteByFormtplId(Long formtplId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formtplId", formtplId);
		return forminitDao.deleteByParams(params);
	}
	
}