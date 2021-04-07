package com.erongdu.server.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.identity.User;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erongdu.common.core.constant.ActivitiConstant;
import com.erongdu.common.core.entity.DictResponse;
import com.erongdu.common.core.entity.activiti.Attachment;
import com.erongdu.common.core.entity.activiti.Forminst;
import com.erongdu.common.core.enums.ForminstNode;
import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.common.core.utils.DateUtil;
import com.erongdu.common.oss.service.OssService;
import com.erongdu.server.activiti.dao.ForminstDao;
import com.erongdu.server.activiti.service.ActHistoryService;
import com.erongdu.server.activiti.service.AttachmentService;
import com.erongdu.server.activiti.service.ForminstService;
import com.erongdu.server.activiti.util.PageableUtil;
import com.erongdu.server.activiti.util.SearchParamsUtil;

/**
 * 表单实例管理
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-7-21
 */
 
@Service("forminstService") 
public class ForminstServiceImpl implements ForminstService{
	
    @Resource
    private ForminstDao forminstDao;
	@Autowired
	private IdentityService identityService;
    @Resource
    private ActHistoryService actHistoryService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private OssService ossService;

	@Override
	public int save(Forminst entity) {
		Date date = new Date();
		if(entity.getId() == null) {
			StringBuilder formName = new StringBuilder(entity.getFormName());
			String username = CommonUtil.getCurrentUsername();
			User user = identityService.createUserQuery().userId(username).singleResult();
			formName.append("-");
			formName.append(StringUtils.isEmpty(user.getFirstName()) ? username : user.getFirstName());
			formName.append("-").append(DateUtil.format(date, "yyyyMMddHHmm"));
			entity.setFormName(formName.toString());
			entity.setUsername(username);
			entity.setCreateTime(date);			
			entity.setEndTime(null);
			return forminstDao.insert(entity);
		}else {
			entity.setLatestUpdateTime(date);
			return forminstDao.update(entity);
		}
	}

	@Override
	public int delete(Long id) {
		Forminst inst = forminstDao.findById(id);
		int record = forminstDao.deleteById(id);
		if(record > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotEmpty(inst.getProcessInstanceId())) {
				params.put("processInstanceId", inst.getProcessInstanceId());
			}else {
				params.put("processInstanceId", ActivitiConstant.ATTACH_FORMID_PRE + id);
			}
			List<Attachment> attaches = attachmentService.list(params);
			if(attaches != null && !attaches.isEmpty()) {
				for (Attachment attach : attaches) {
					try {
						ossService.remove(attach.getTargetName());
						attachmentService.delete(attach.getId());
					}catch (Exception e) {
					}
				}
			}
		}
		return record;
	}

	@Override
	public Map<String,Object> page(Map<String, Object> params, Pageable pageable) {
		IPage<Forminst> viewPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());	
		IPage<Forminst> result = forminstDao.pageSelective(viewPage, SearchParamsUtil.wrapper(params));
    	Map<String, String> formUsers = new HashMap<String, String>();
    	for (Forminst fi : result.getRecords()) {
    		String username = fi.getUsername();
			if(!formUsers.containsKey(username)) {
				User user = identityService.createUserQuery().userId(username).singleResult();
				formUsers.put(username, user.getFirstName());
			}
			fi.setFirst(formUsers.get(username));
		}
        return PageableUtil.toPage(result.getRecords(), result.getTotal());
	}
	
	@Override
	public List<DictResponse> search(Map<String, Object> params) {
		List<Forminst> list = forminstDao.listSelective(params);
		List<DictResponse> result = new ArrayList<DictResponse>();
		for (Forminst fi : list) {
			result.add(new DictResponse(fi.getFormName(), fi.getId().toString()));
		}
		return result;
	}
	
	@Override
	public List<Forminst> list(Map<String, Object> params) {
		return forminstDao.listSelective(params);
	}

	@Override
	public Forminst findById(Long id) {
		return forminstDao.findById(id);
	}
	
	@Override
	public Forminst findByInstanceId(String processInstanceId) {
		return forminstDao.findByInstanceId(processInstanceId);
	}
	
	@Override
	public void updateNodeName(Map<String, Object> params) {
		String nodeName = MapUtils.getString(params, "nodeName");
		if(nodeName != null && 
				(ForminstNode.STOPED.eqDesc(nodeName) || ForminstNode.END.eqDesc(nodeName))) {
    		params.put("endTime", new Date());
    	}
		if(StringUtils.isNotEmpty(nodeName)) {
			forminstDao.updateNodeName(params);
		}
	}
	
	@Override
	public void updateNodeName(String processInstanceId, String executionId) {
		// 变更表单实例任务节点
    	HistoricActivityInstance nextTask = actHistoryService.nextUserActivity(executionId);
    	String nodeName = nextTask == null ? (actHistoryService.finished(processInstanceId) ?"已归档" : "----"): nextTask.getActivityName();
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("processInstanceId", processInstanceId);
    	params.put("nodeName", nodeName);    	
    	updateNodeName(params);
	}
	
	@Override
	public int count(Map<String, Object> params) {
		return forminstDao.countSelective(params);
	}
}