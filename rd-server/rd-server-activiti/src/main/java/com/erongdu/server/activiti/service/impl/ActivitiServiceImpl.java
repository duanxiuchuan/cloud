package com.erongdu.server.activiti.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

import org.activiti.bpmn.model.StartEvent;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.erongdu.common.core.constant.ActivitiConstant;
import com.erongdu.common.core.entity.activiti.Attachment;
import com.erongdu.common.core.entity.activiti.Category;
import com.erongdu.common.core.entity.activiti.Forminst;
import com.erongdu.common.core.entity.activiti.Formtpl;
import com.erongdu.common.core.entity.activiti.ProcdefForm;
import com.erongdu.common.core.enums.ForminstNode;
import com.erongdu.common.core.enums.FormulaType;
import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.common.core.utils.DateUtil;
import com.erongdu.common.oss.service.OssService;
import com.erongdu.server.activiti.dao.ProcdefFormDao;
import com.erongdu.server.activiti.model.form.ColumnEle;
import com.erongdu.server.activiti.model.form.FormEle;
import com.erongdu.server.activiti.model.form.GroupEle;
import com.erongdu.server.activiti.model.request.DefinitionStartableRequest;
import com.erongdu.server.activiti.model.request.DelegateTaskRequest;
import com.erongdu.server.activiti.model.request.DeploymentRequest;
import com.erongdu.server.activiti.model.request.ProcessStartRequest;
import com.erongdu.server.activiti.model.request.ProcessStopRequest;
import com.erongdu.server.activiti.model.request.SubTaskRequest;
import com.erongdu.server.activiti.model.request.TaskCompleteRequest;
import com.erongdu.server.activiti.model.request.TaskRequest;
import com.erongdu.server.activiti.model.response.CommentResponse;
import com.erongdu.server.activiti.model.response.DeploymentResponse;
import com.erongdu.server.activiti.model.response.HistoricActivityResponse;
import com.erongdu.server.activiti.model.response.ProcessDefinitionResponse;
import com.erongdu.server.activiti.model.response.SequenceFlowResponse;
import com.erongdu.server.activiti.model.response.TaskResponse;
import com.erongdu.server.activiti.service.ActFlowElementService;
import com.erongdu.server.activiti.service.ActHistoryService;
import com.erongdu.server.activiti.service.ActRepositoryService;
import com.erongdu.server.activiti.service.ActTaskService;
import com.erongdu.server.activiti.service.ActivitiService;
import com.erongdu.server.activiti.service.AttachmentService;
import com.erongdu.server.activiti.service.ForminitService;
import com.erongdu.server.activiti.service.ForminstService;
import com.erongdu.server.activiti.service.FormtplService;
import com.erongdu.server.activiti.service.FormulaService;
import com.erongdu.server.activiti.util.ActivitiUtil;
import com.erongdu.server.activiti.util.AttachmentUtil;
import com.erongdu.server.activiti.util.PageableUtil;


@Service  
public class ActivitiServiceImpl implements ActivitiService{  
	
	private static final Logger logger = LoggerFactory.getLogger(ActivitiServiceImpl.class);
	

	
	@Autowired
	private RepositoryService repositoryService;
    @Autowired  
    private RuntimeService runtimeService;  
    @Autowired  
    private TaskService taskService; 
    @Autowired
    private HistoryService historyService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private ProcdefFormDao procdefFormDao;
    @Autowired
    private FormtplService formtplService;
    @Autowired
    private ForminstService forminstService;
    @Autowired
    private ActFlowElementService actFlowElementService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ActHistoryService actHistoryService;
    @Autowired
    private ActRepositoryService actRepositoryService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private ForminitService forminitService;
    @Autowired
    private FormulaService formulaService;
    @Autowired
    private OssService ossService;
    
    /**
	 * deploy resource
	 * 
	 * @param name resource model
	 * @param fin  resource inputstream
	 * @return
	 */
    @Override
	public String deploy(DeploymentRequest model, InputStream fin) {
    	String name = model.getName();
    	if (!name.endsWith(".bpmn20.xml") && !name.endsWith(".bpmn")) {
            name = name + ".bpmn";
        }
		ZipInputStream zin = null;
		Deployment deployment = null;
		try {
			if(model.getSourceName().endsWith(".zip")) {
				zin = new ZipInputStream(fin);
				deployment = repositoryService.createDeployment()
						.addZipInputStream(zin)
						.name(name).category(model.getCategory())
						.deploy();	
			} else {
				deployment = repositoryService.createDeployment()
						.addInputStream(name, fin)
						.name(name).category(model.getCategory())
						.deploy();			
			}
						
		}finally {
			try {
				if(zin != null) zin.close();
				if(fin != null) fin.close();
			} catch (IOException e) {}
		}
		
		ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).latestVersion().singleResult();
		String processDefinitionId = definition.getId();		
		//?????????????????????????????????
		repositoryService.setProcessDefinitionCategory(processDefinitionId, model.getCategory());
		
		// ??????form??????
		if(StringUtils.isNotEmpty(model.getFormKey())) {
			logger.info("processDefinitionId:{}", processDefinitionId);
			ProcdefForm pf = new ProcdefForm(processDefinitionId, model.getFormKey());
			procdefFormDao.insert(pf);	
		}
		return deployment.getId();
	}
    

    /**
     * ????????????????????????
     */
    @Override
    public Map<String, Object> queryDeployments(Map<String, Object> params, Pageable pageable) {
    	DeploymentQuery query = repositoryService.createDeploymentQuery();
    	String deploymentName = MapUtils.getString(params, "deploymentName");
    	if(StringUtils.isNotEmpty(deploymentName)) {
    		 query.deploymentNameLike(ActivitiUtil.like(deploymentName));
    	}
    	String processDefinitionKey = MapUtils.getString(params, "processDefinitionKey");
        if(StringUtils.isNotEmpty(processDefinitionKey)) {
         	query.processDefinitionKeyLike(ActivitiUtil.like(processDefinitionKey));
        }
    	long total = query.count();
    	int firstResult = PageableUtil.offset(pageable);
        List<Deployment> page = query.listPage(firstResult, pageable.getPageSize());
        List<DeploymentResponse> data = new ArrayList<DeploymentResponse>(page.size());
        if(page.size() > 0) {
        	for (Deployment deployment : page) {
        		DeploymentResponse model = new DeploymentResponse();
        		BeanUtils.copyProperties(deployment, model);
        		data.add(model);
        	}
        }        
        return PageableUtil.toPage(data, total);
    }
    
    /**
     * ????????????????????????
     */
    @Override
    public Map<String, Object> queryProcessDefinitions(Map<String, Object> params, Pageable pageable) {
        ProcessDefinitionQuery query = queryProcessDefinition(params);
        
        long total = query.count();
        int firstResult = PageableUtil.offset(pageable);
        List<ProcessDefinition> page = query.listPage(firstResult, pageable.getPageSize());
        List<ProcessDefinitionResponse> data = new ArrayList<>(page.size());
        if(page.size() > 0) {
        	for (ProcessDefinition pd : page) {
        		ProcessDefinitionResponse model = new ProcessDefinitionResponse();
        		BeanUtils.copyProperties(pd, model);
        		// ??????????????????????????????
        		actRepositoryService.queryProcessCandicate(pd.getId(), model);
        		
        		data.add(model);
        	}
        }        
        return PageableUtil.toPage(data, total);
    }
    
    @Override
    public Map<String, Object> queryProcessList(Map<String, Object> params, Pageable pageable) {
    	return actRepositoryService.queryProcessList(params, pageable);
    }
    
	private ProcessDefinitionQuery queryProcessDefinition(Map<String, Object> params) {
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        // key like 
        String key = MapUtils.getString(params, "key");
        if(StringUtils.isNotEmpty(key)) {
        	query.processDefinitionKeyLike(ActivitiUtil.like(key));
        }
        // name like
        String name = MapUtils.getString(params, "name");
        if(StringUtils.isNotEmpty(name)) {
        	query.processDefinitionNameLike(ActivitiUtil.like(name));
        }
        // category like
        String category = MapUtils.getString(params, "category");
        if(StringUtils.isNotEmpty(category) && !Category.NAME_ALL.equals(category)) {
        	query.processDefinitionCategoryLike(ActivitiUtil.like(category));
        }
		return query;
	}
    
    /**
     * ??????????????????
     */
    @Override
    public Map<String, Object> prepareProcess(ProcessStartRequest model) {
    	Map<String, Object> data = new HashMap<String, Object>();
    	String processDefinitionId = model.getProcessDefinitionId();
    	Formtpl formTpl = formtplService.findByProcessDefinitionId(processDefinitionId);
    	Forminst form = new Forminst();
    	BeanUtils.copyProperties(formTpl, form);
    	form.setId(null);
    	form.setFormtplId(formTpl.getId());
    	
    	String groups = null;//????????????
    	if(formTpl.getGrouped()) {//?????????????????????????????????????????????
    		StartEvent startEvent = actFlowElementService.getStartEvent(processDefinitionId);
    		groups = startEvent.getFormKey();
    	}
    	// ??????????????????
    	formAutoFill(form, groups); 
    	
    	// ????????????
    	if(formTpl!= null && formTpl.getId() != null) {
    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("formtplId", formTpl.getId());
    		params.put("type", FormulaType.COL.getCode());
    		data.put("colFormulas", formulaService.formula(params));    	
    		params.put("type", FormulaType.ROW.getCode());
    		data.put("rowFormulas", formulaService.formula(params));
    	}
    	
    	data.put("form", form);
    	data.put("groups", groups);
    	
    	return data;
    }
    
    /** 
     * ????????????
     * 
     * @return processDefinitionKey
     */  
    @SuppressWarnings("unchecked")
	@Override
    public String startProcess(ProcessStartRequest model) {   
    	
    	String assignee = CommonUtil.getCurrentUsername();
    	Map<String, Object> variables = model.getVariables();
    	if(variables.isEmpty() && StringUtils.isNotEmpty(model.getVariablesStr())) {
    		variables = JSON.parseObject(model.getVariablesStr(), Map.class);
    	}
    	String processDefinitionId = model.getProcessDefinitionId();
    	
    	// ??????????????????
    	boolean startFlag = startable(assignee, processDefinitionId);
    	if(!startFlag) {
    		throw new RuntimeException("???????????????????????????");
    	}
    	
    	//??????????????????
    	Map<String, Object> instanceVariables = fillVariablesWithForm(variables, processDefinitionId, null);
    	instanceVariables.put(ActivitiConstant.VAR_APPLYUSERID, assignee);
    	
    	Forminst forminst = null;
    	if(StringUtils.isNotEmpty(model.getFormKey()) && variables != null && !variables.isEmpty()) {
    		forminst = saveForminst(model, variables);    		
    	}
    	String processInstanceId = null;
    	Task task = null;
    	if(model.getSubmit()) {
    		if(StringUtils.isNotEmpty(model.getAssociate())) {
    			instanceVariables.put(ActivitiConstant.VAR_ASSOCIATE, model.getAssociate());
    		}
    		
    		// ?????????????????????????????????ID???????????????????????????ID?????????activiti:initiator???
    		identityService.setAuthenticatedUserId(assignee);
    		
    		String businessKey = model.getFormKey() + ":" + (forminst == null ? "" : forminst.getId()); 
    		ProcessInstance instance = runtimeService.startProcessInstanceById(processDefinitionId, 
    				businessKey, instanceVariables);
    		if(forminst != null) {// ??????????????????????????????????????????
    			forminst.setProcessInstanceId(instance.getId());
    			forminst.setProcessDefinitionId(instance.getProcessDefinitionId());
    			forminstService.save(forminst);
    		}
    		// ?????????Task???????????????????????????????????????????????????
    		List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getId()).list();
    		for (Task _task : tasks) {
    			if(task != null && !actTaskService.candidate(_task)) {// ????????????????????????
    				taskService.resolveTask(_task.getId(), instanceVariables);
    				taskService.claim(_task.getId(), assignee);    		
    			}
    		}
    		task = tasks.isEmpty() ? null : tasks.get(0);
    	}else {
    		processInstanceId = ActivitiConstant.ATTACH_FORMID_PRE + forminst.getId();
    	}
    	
    	// ??????????????????
    	if(forminst != null && task != null) {
    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("formId", forminst.getId());
    		params.put("processInstanceId", processInstanceId);
    		params.put("nodeName", task == null ? ForminstNode.START.getDesc(): task.getName());
    		forminstService.updateNodeName(params);
    	}
    	
    	// ????????????
    	MultipartFile[] files = model.getFile();
    	if(model.getForminstId() != null) {//
    		Map<String, Object> attachParams = new HashMap<String, Object>();
    		attachParams.put("processDefinitionId", processDefinitionId);
    		attachParams.put("processInstanceId", ActivitiConstant.ATTACH_FORMID_PRE + model.getForminstId());
    		List<Attachment> attaches = attachmentService.list(attachParams);
    		for (Attachment attach : attaches) {
    			attachmentService.delete(attach.getId());
    			try {
    				ossService.remove(attach.getTargetName());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}    		
    	}
    	upload(files, "startEvent", processDefinitionId, processInstanceId);
    	
        return processInstanceId;  
    }

    /**
     * ??????????????????
     * @param assignee
     * @param processDefinitionId
     * @return
     */
	private boolean startable(String assignee, String processDefinitionId) {
		//???????????????????????????
    	List<IdentityLink> links = repositoryService.getIdentityLinksForProcessDefinition(processDefinitionId);
    	boolean startable = false;
    	if(links == null || links.isEmpty()) {
    		startable = true;
    	}else {
    		List<Group> groups = identityService.createGroupQuery().groupMember(assignee).list();
    		for (IdentityLink link : links) {
				if(StringUtils.isNotEmpty(link.getUserId())) {
					if(link.getUserId().equals(assignee)) {
						startable = true;
						break;
					}
				}
    			if(StringUtils.isNotEmpty(link.getGroupId())) {
					for (Group group : groups) {
						if(group.getId().equals(link.getGroupId())) {
							startable = true;
							break;
						}
					}
				}				
			}
    	}
    	return startable;
	}

	/**
	 * ??????????????????
	 * @param formVariables
	 * @param processDefinitionId
	 * @param taskDefinitionKey
	 * @return
	 */
	private Map<String, Object> fillVariablesWithForm(Map<String, Object> formVariables, String processDefinitionId, String taskDefinitionKey) {
		Map<String, Object> instanceVariables = new HashMap<String, Object>();
		Set<String> labels = new HashSet<String>();
		if(StringUtils.isEmpty(taskDefinitionKey)) {
			 labels = actFlowElementService.getFlowConditionVariables(processDefinitionId);
		}else {
			List<SequenceFlowResponse> flows = actFlowElementService.getOutgoingFlowsByTaskKey(processDefinitionId, taskDefinitionKey); 
			for (SequenceFlowResponse flow : flows) {
				Set<String> vs = actFlowElementService.getExpressionVariables(flow.getConditionExpression());
				if(!vs.isEmpty()) {
					labels.addAll(vs);
				}
			}
		}
		for (String label : labels) {
			Object formValue = formVariables.get(label);
			if(formValue == null) {
				continue;
			}
			instanceVariables.put(label, formValue);
		}
    	return instanceVariables;
	}

    /**
     * ??????????????????
     * @param formKey
     * @param processDefinitionId
     * @param processInstanceId
     * @param variables
     */
	private Forminst saveForminst(ProcessStartRequest model, Map<String, Object> variables) {
		Forminst forminst = null;
		if(model.getForminstId() != null) {
			forminst = forminstService.findById(model.getForminstId());
		}else {
			ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(model.getProcessDefinitionId()).singleResult();  
			Formtpl formTpl = formtplService.findByFormKey(model.getFormKey());
			forminst = new Forminst();
			forminst.setAssociateFlag(formTpl.getAssociateFlag());
			forminst.setLinkedFlag(formTpl.getLinkedFlag());
			forminst.setFormKey(formTpl.getFormKey());
			forminst.setFormtplId(formTpl.getId());
			forminst.setGrouped(formTpl.getGrouped());
			forminst.setFormName(formTpl.getFormName());
			forminst.setFormContent(formTpl.getFormContent());	
			forminst.setCategory(definition.getCategory());
		}		
		forminst.setAssociate(model.getAssociate());
		forminst.setLinkedId(model.getLinkedId());
		forminst.setLinkedName(model.getLinkedName());
		forminst.setProcessDefinitionId(model.getProcessDefinitionId());		
		forminst.setProcessInstanceId(null);
		forminst.setVariables(JSONObject.toJSONString(variables));
		forminst.setNodeName(ForminstNode.START.getDesc());
		forminstService.save(forminst);
		return forminst;
	}  
    
    /** 
     * ??????????????????
     * 
     * @return user task list 
     */  
    @Override
    public Map<String, Object> queryTasks(Map<String, Object> params, Pageable pageable) {
    	String username = CommonUtil.getCurrentUsername(); 
    	TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned(username);
    	String category = MapUtils.getString(params, "category");
    	if(StringUtils.isNotEmpty(category) && !Category.NAME_ALL.equals(category)) {
    		query.taskCategory(category);
    	}
    	String taskName = MapUtils.getString(params, "name");
    	if(StringUtils.isNotEmpty(taskName)) {
    		query.taskNameLike(ActivitiUtil.like(taskName));
    	}
    	
    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String startTime = MapUtils.getString(params, "startTime");
    	if(StringUtils.isNotEmpty(startTime)) {
    		try {
				query.taskCreatedAfter(dateformat.parse(DateUtil.toDateStart(startTime)));
			} catch (ParseException e) {}    		
    	}
    	
    	String endTime = MapUtils.getString(params, "endTime");
    	if(StringUtils.isNotEmpty(endTime)) {
    		try {
				query.taskCreatedBefore(dateformat.parse(DateUtil.toDateEnd(endTime)));
			} catch (ParseException e) {}    		
    	}
    	
    	long total = query.count();    	
    	int firstResult = PageableUtil.offset(pageable);
    	List<Task> tasks = query.orderByTaskCreateTime().asc().listPage(firstResult, pageable.getPageSize());
        List<TaskResponse> data = new ArrayList<TaskResponse>();
        Map<String, Forminst> forminsts = new HashMap<String, Forminst>(); 
        for (Task task : tasks) {
        	TaskResponse model = new TaskResponse();
        	BeanUtils.copyProperties(task, model);
        	// forminst
    		String processInstanceId = task.getProcessInstanceId();
    		
    		if(StringUtils.isNotEmpty(task.getParentTaskId())) {
        		JSONObject desc = JSONObject.parseObject(task.getDescription());
        		processInstanceId = desc.getString(ActivitiConstant.KEY_PROCESS_INSTANCE_ID);
        	}
    		Forminst forminst = null;
    		if(!forminsts.containsKey(processInstanceId)) {
    			forminst = forminstService.findByInstanceId(processInstanceId);
    			forminsts.put(processInstanceId, forminst);
    		}else {
    			forminst = forminsts.get(processInstanceId);
    		}   
    		if(forminst != null) {
    			model.setFormName(forminst.getFormName());
    			model.setFormCreateTime(forminst.getCreateTime());
    			model.setNodeName(forminst.getNodeName());  
    		}
        	model.setProcessInstanceId(processInstanceId);
        	data.add(model);
		}        
        return PageableUtil.toPage(data, total);
    }  
    
    /**
     * ??????????????????
     * @param uid
     * @param pageable
     * @return
     */
    @Override
    public Map<String, Object> queryHisTasks(Map<String, Object> params,Pageable pageable){    	
    	String username = CommonUtil.getCurrentUsername();
    	HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().taskAssignee(username);
    	
    	String category = MapUtils.getString(params, "category");
    	if(StringUtils.isNotEmpty(category) && !Category.NAME_ALL.equals(category)) {
    		query.taskCategory(category);
    	}
    	String taskName = MapUtils.getString(params, "name");
    	if(StringUtils.isNotEmpty(taskName)) {
    		query.taskNameLike(ActivitiUtil.like(taskName));
    	}
    	
    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	// ????????????
    	String startTime = MapUtils.getString(params, "startTime");
    	if(StringUtils.isNotEmpty(startTime)) {
    		try {
				query.taskCreatedAfter(dateformat.parse(DateUtil.toDateStart(startTime)));
			} catch (ParseException e) {}    		
    	}
    	// ????????????
    	String endTime = MapUtils.getString(params, "endTime");
    	if(StringUtils.isNotEmpty(endTime)) {
    		try {
				query.taskCreatedBefore(dateformat.parse(DateUtil.toDateEnd(endTime)));
			} catch (ParseException e) {}    		
    	}
    	
    	// ????????????
    	String completedStart = MapUtils.getString(params, "completedStart");
    	if(StringUtils.isNotEmpty(completedStart)) {
    		try {
				query.taskCompletedAfter(dateformat.parse(DateUtil.toDateStart(completedStart)));
			} catch (ParseException e) {}    		
    	}
    	// ????????????
    	String completedEnd = MapUtils.getString(params, "completedEnd");
    	if(StringUtils.isNotEmpty(completedEnd)) {
    		try {
				query.taskCompletedBefore(dateformat.parse(DateUtil.toDateEnd(completedEnd)));
			} catch (ParseException e) {}    		
    	}
    	
    	
    	long total = query.count();
    	List<TaskResponse> data = new ArrayList<TaskResponse>();
    	int firstResult = PageableUtil.offset(pageable);
    	List<HistoricTaskInstance> list = query.orderByHistoricTaskInstanceEndTime().desc().listPage(firstResult, pageable.getPageSize());
    	Map<String, Forminst> forminsts = new HashMap<String, Forminst>(); 
    	for (HistoricTaskInstance task : list) {
    		TaskResponse model = new TaskResponse();
    		BeanUtils.copyProperties(task, model);
    		// forminst
    		String processInstanceId = task.getProcessInstanceId();
    		if(StringUtils.isNotEmpty(task.getParentTaskId())) {
        		JSONObject desc = JSONObject.parseObject(task.getDescription());
        		processInstanceId = desc.getString(ActivitiConstant.KEY_PROCESS_INSTANCE_ID);
        	}
    		
    		Forminst forminst = null;
    		if(!forminsts.containsKey(processInstanceId)) {
    			forminst = forminstService.findByInstanceId(processInstanceId);
    			forminsts.put(processInstanceId, forminst);
    		}else {
    			forminst = forminsts.get(processInstanceId);
    		}    		
    		if(forminst != null) {
    			model.setFormName(forminst.getFormName());
    			model.setFormCreateTime(forminst.getCreateTime());
    			model.setNodeName(forminst.getNodeName());    
    		}
        	model.setProcessInstanceId(processInstanceId);
        	data.add(model);
		}
    	return PageableUtil.toPage(data, total);
    }
    
    /**
     * ????????????
     */
    @Override
    public Map<String, Object> queryTodoTaskById(String taskId) {
    	String username = CommonUtil.getCurrentUsername();
    	Map<String, Object> result = new HashMap<String, Object>();
    	Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    	TaskResponse tr = new TaskResponse();
    	BeanUtils.copyProperties(task, tr);
    	result.put("task", tr);
    	String processInstanceId = task.getProcessInstanceId();
    	String processDefinitionId = task.getProcessDefinitionId();
    	if(StringUtils.isNotEmpty(task.getParentTaskId())) {
    		JSONObject desc = JSONObject.parseObject(task.getDescription());
    		processInstanceId = desc.getString(ActivitiConstant.KEY_PROCESS_INSTANCE_ID);
    		processDefinitionId = desc.getString(ActivitiConstant.KEY_PROCESS_DEFINITION_ID);
    	}
    	// ??????????????????
    	Forminst form = forminstService.findByInstanceId(processInstanceId);
    	// ??????????????????
    	if(StringUtils.isNotEmpty(task.getFormKey())) {
    		formAutoFill(form, task.getFormKey());	
    	}
    	result.put("form", form);   
    	
    	// ????????????
    	if(form != null && form.getFormtplId() != null) {
    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("formtplId", form.getFormtplId());
    		params.put("type", FormulaType.COL.getCode());
    		result.put("colFormulas", formulaService.formula(params));
    		params.put("type", FormulaType.ROW.getCode());
    		result.put("rowFormulas", formulaService.formula(params));
    	}
    	
    	boolean delegateTask = false; //?????????????????????
    	if(task.getDelegationState() == DelegationState.PENDING && !username.equals(task.getOwner())) {// ???????????? ????????????
    		delegateTask = true;
    	}else if(task.getParentTaskId()!= null) {
    		result.put("flows", null);  
    	}else {
    		// ????????????????????????    	
    		List<SequenceFlowResponse> flows = actFlowElementService.getOutgoingFlowsByTaskKey(
    				processDefinitionId, task.getTaskDefinitionKey());    
    		int virtualFlowIndex = 0;
    		List<SequenceFlowResponse> fs = new ArrayList<SequenceFlowResponse>();
    		for (SequenceFlowResponse flow : flows) {
    			String expression = flow.getConditionExpression();
    			if(StringUtils.isEmpty(expression)) {
    				continue;
    			}
				Set<String> vars = actFlowElementService.getExpressionVariables(flow.getConditionExpression());
				if(!vars.isEmpty()) {
					if(vars.size() == 1 && vars.contains(ActivitiConstant.KEY_OUTCOME)) {
						fs.add(flow);
					}else if(virtualFlowIndex++ == 0) {
						flow.setName(ActivitiConstant.DEF_FLOW_NAME);
						fs.add(flow);
					}
				}
			}
    		result.put("flows", fs);    		
    	}
    	result.put("delegateTask", delegateTask);   
    	
    	// ??????????????????
    	List<CommentResponse> comments = actTaskService.getCommentsByProcessInstanceId(processInstanceId);
    	result.put("comments", comments);
    	
    	// ????????????????????? 
    	boolean formEditable = false;    	
    	if(form.getGrouped()) {//????????????????????????????????????
    		result.put("groups", task.getFormKey());
    	}else if(form.getFormKey().equalsIgnoreCase(task.getFormKey())) {
    		formEditable = true;
    	}
    	result.put("formEditable", formEditable);  
    	
    	// ????????????
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("processInstanceId", processInstanceId);
    	List<Attachment> attachments = attachmentService.list(params);
    	result.put("attachments", attachments);  
    	// ?????????????????????
    	result.put("nextCandidate", actFlowElementService.getNextTaskCandidate(processDefinitionId, task.getTaskDefinitionKey()));
    	
    	return result;
    }
    
    
    private void formAutoFill(Forminst forminst, String groups) {
    	
    	// ????????????
    	FormEle formEle = FormEle.of(forminst.getFormContent());
    	List<ColumnEle> columns = new ArrayList<ColumnEle>();
    	
    	if(!formEle.isGroup()) {// ?????????
    		columns.addAll(formEle.getColumn());
    	}else {//??????
    		if(StringUtils.isEmpty(groups)) {
    			return;
    		}
    		// ????????????????????????
			String[] gs = groups.split(",");
			for (String groupLabel : gs) {
				for (GroupEle ge : formEle.getGroup()) {
					if(ge.getLabel().equalsIgnoreCase(groupLabel.trim())) {
						columns.addAll(ge.getColumn());
					}
				}
			}
    	}
    	if(columns.isEmpty()) {
    		return;
    	}
    	
    	// ????????????????????????
    	String variables = forminst.getVariables();
    	forminst.setVariables(forminitService.init(forminst.getFormtplId(), variables, columns));    	
    }
    
    /**
     * ????????????
     */
    @Override
    public Map<String, Object> queryHisTaskById(String taskId) {
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	// ????????????
    	HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
    	TaskResponse tr = new TaskResponse();
    	BeanUtils.copyProperties(task, tr);
    	result.put("task", tr);
    	
    	String processInstanceId = task.getProcessInstanceId();
    	if(StringUtils.isNotEmpty(task.getParentTaskId())) {
    		JSONObject desc = JSONObject.parseObject(task.getDescription());
    		processInstanceId = desc.getString(ActivitiConstant.KEY_PROCESS_INSTANCE_ID);
    	}    	
    	
    	// ??????????????????
    	Forminst form = forminstService.findByInstanceId(processInstanceId);
    	result.put("form", form);   
    	
    	// ??????????????????
    	List<CommentResponse> comments = actTaskService.getCommentsByProcessInstanceId(processInstanceId);
    	result.put("comments", comments);
    	
    	return result;
    }
    
    @Override
    public Map<String, Object> queryForminst(Long formId) {
    	String username = CommonUtil.getCurrentUsername();
    	Map<String, Object> result = new HashMap<String, Object>();
    	// ??????????????????
    	Forminst form = forminstService.findById(formId);
    	result.put("form", form);   
    	if(form == null) {
    		return result;
    	}

    	// ????????????
    	if(form != null && form.getFormtplId() != null) {
    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("formtplId", form.getFormtplId());
    		params.put("type", FormulaType.COL.getCode());
    		result.put("colFormulas", formulaService.formula(params));
    		params.put("type", FormulaType.ROW.getCode());
    		result.put("rowFormulas", formulaService.formula(params));
    	}
    	
    	String processInstanceId = form.getProcessInstanceId();
    	if(StringUtils.isEmpty(processInstanceId)) {
    		result.put("formEditable", true); 
    		result.put("auditEnable", false); 
        	result.put("draft", true); 
        	Map<String, Object> params = new HashMap<String, Object>();
        	params.put("processInstanceId", ActivitiConstant.ATTACH_FORMID_PRE + form.getId());
        	List<Attachment> attachments = attachmentService.list(params);
        	result.put("attachments", attachments);
        	String groups = null;//????????????
        	if(form.getGrouped()) {//?????????????????????????????????????????????
        		StartEvent startEvent = actFlowElementService.getStartEvent(form.getProcessDefinitionId());
        		groups = startEvent.getFormKey();
        	}
        	result.put("groups", groups);    
    		return result;
    	}
    	result.put("draft", false); 
    	
    	// ??????????????????
    	List<CommentResponse> comments = actTaskService.getCommentsByProcessInstanceId(processInstanceId);
    	result.put("comments", comments);    	
    	
    	// ?????????????????????????????????
    	List<Task> tasks = taskService.createTaskQuery().processInstanceId(form.getProcessInstanceId()).orderByTaskCreateTime().asc().list();
    	// ????????????
    	TaskResponse tr = new TaskResponse();
    	// ???????????????????????????????????????
    	boolean formEditable = false, auditEnable = false;    	
    	if(tasks != null && tasks.size() > 0 ) {
    		Task task = tasks.get(0);
    		auditEnable = actTaskService.candidate(task, username);
        	BeanUtils.copyProperties(task, tr);
        	
        	if(form.getGrouped()) {//????????????????????????????????????
        		result.put("groups", task.getFormKey());
        	}else if(form.getFormKey().equalsIgnoreCase(task.getFormKey())) {
        		formEditable = true;
        	}        	
    	}
    	result.put("formEditable", formEditable); 
    	result.put("auditEnable", auditEnable);  
    	result.put("task", tr);    
    	
       	// ????????????
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("processInstanceId", processInstanceId);
    	List<Attachment> attachments = attachmentService.list(params);
    	result.put("attachments", attachments);
    	
    	
    	return result;
    }
    

	
    /**
     * ??????????????????????????????????????????
     */
	@Override
	public void delegateTask(DelegateTaskRequest task) {
		String assignee = CommonUtil.getCurrentUsername();
		String taskId = task.getTaskId();
		Task dbtask = taskService.createTaskQuery().taskId(taskId).singleResult();
		if(dbtask.getParentTaskId() != null) {
			throw new RuntimeException("?????????????????????????????????");
		}
		
		if(StringUtils.isEmpty(dbtask.getOwner())) {	
			taskService.setOwner(taskId, assignee);
		}
		
		taskService.delegateTask(task.getTaskId(), task.getDelegateAssignee());
		if(task.isCommentFlag() && StringUtils.isNotEmpty(task.getProcessInstanceId())) {
			// ????????????
			identityService.setAuthenticatedUserId(assignee);		
			User delegateUser = identityService.createUserQuery().userId(task.getDelegateAssignee()).singleResult();
			String message = String.format("??????????????????%s???????????????", 
					delegateUser.getFirstName() == null ? task.getDelegateAssignee() : delegateUser.getFirstName());
			taskService.addComment(task.getTaskId(), task.getProcessInstanceId(), message);
		}
	}
	
	/**
	 * ??????????????????????????????????????????
	 */
	@Override
	public void transferTask(TaskRequest model) {		
		Task dbtask = taskService.createTaskQuery().taskId(model.getTaskId()).singleResult();
		if(dbtask.getParentTaskId() != null) {
			throw new RuntimeException("?????????????????????????????????");
		}
		
		Task task = actTaskService.saveTask(model);
		if(task.getParentTaskId() != null) {
		}
		model.setProcessInstanceId(task.getProcessInstanceId());
		model.setComment("??????????????????%s???????????????");
		// ????????????
		addTaskComment(model);
	}

	/**
	 * ??????????????????????????????????????????
	 */
	@Override
	public void ccTask(TaskRequest model) {
		Task task = taskService.createTaskQuery().taskId(model.getTaskId()).singleResult();
		if(task.getParentTaskId() != null) {
			throw new RuntimeException("?????????????????????????????????");
		}
		SubTaskRequest sub = new SubTaskRequest();
		sub.setAssignee(model.getAssignee());
		sub.setOwner(model.getAssignee());
		sub.setName("????????????"+ task.getName());
		sub.setParentTaskId(model.getTaskId());
		
		JSONObject description = new JSONObject();
		description.put(ActivitiConstant.KEY_PROCESS_INSTANCE_ID, task.getProcessInstanceId());
		description.put(ActivitiConstant.KEY_PROCESS_DEFINITION_ID, task.getProcessDefinitionId());
		sub.setDescription(description.toJSONString());
		
		actTaskService.createSubTask(sub);
		model.setProcessInstanceId(task.getProcessInstanceId());
		// ????????????
		model.setComment("?????????????????????%s???");
		addTaskComment(model);
	}
	
	private void addTaskComment(TaskRequest model) {
		if(model.isCommentFlag() && StringUtils.isNotEmpty(model.getProcessInstanceId())) {
			// ????????????
			identityService.setAuthenticatedUserId(CommonUtil.getCurrentUsername());		
			User delegateUser = identityService.createUserQuery().userId(model.getAssignee()).singleResult();
			String message = String.format(model.getComment(), 
					delegateUser.getFirstName() == null ? model.getAssignee() : delegateUser.getFirstName());
			taskService.addComment(model.getTaskId(), model.getProcessInstanceId(), message);
		}
	}
	
    
    @SuppressWarnings("unchecked")
	@Override
    public void doTask(TaskCompleteRequest model) {
    	String taskId = model.getTaskId();
    	String assignee = CommonUtil.getCurrentUsername() ;
    	
    	Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    	String processDefinitionId = task.getProcessDefinitionId();
    	String processInstanceId = task.getProcessInstanceId();
    	if(StringUtils.isNotEmpty(task.getParentTaskId())) {
    		JSONObject desc = JSONObject.parseObject(task.getDescription());
    		processInstanceId = desc.getString(ActivitiConstant.KEY_PROCESS_INSTANCE_ID);
    		processDefinitionId = desc.getString(ActivitiConstant.KEY_PROCESS_DEFINITION_ID);
    	}
    	// ????????????
    	MultipartFile[] files = model.getFile();
    	upload(files, taskId, processDefinitionId, processInstanceId);
    	
    	// ????????????
    	String comment = StringUtils.isEmpty(model.getComment()) ? model.getOutcome() : model.getComment() ;
    	if(StringUtils.isNotEmpty(comment)) {
    		identityService.setAuthenticatedUserId(assignee);
    		taskService.addComment(taskId, processInstanceId, comment);
    	}
    	
    	if(task.getDelegationState() == DelegationState.PENDING 
    			&& !assignee.equalsIgnoreCase(task.getOwner())) {// ???????????? ????????????
    		taskService.delegateTask(taskId, task.getOwner());    		
    		return ;
    	}
    	
    	// ???????????????
    	Map<String, Object> viewVariables = model.getVariables();
    	Map<String, Object> instanceVariables = new HashMap<String, Object>();    	
    	if(viewVariables.isEmpty() && StringUtils.isNotEmpty(model.getVariablesStr())) {
    		viewVariables = JSONObject.parseObject(model.getVariablesStr(), Map.class);
    	}
    	if(!viewVariables.isEmpty()) {
    		// ??????????????????????????????????????????
    		instanceVariables = fillVariablesWithForm(viewVariables, processDefinitionId, null);
    	}
    	
    	// ????????????????????????
    	if(StringUtils.isNotEmpty(task.getFormKey())) {//
    		if(viewVariables != null && !viewVariables.isEmpty()) {
    			Forminst forminst = forminstService.findByInstanceId(processInstanceId);
    			if(forminst != null) {
    				Map<String, Object> vars = new HashMap<String, Object>();
    				if(StringUtils.isNotEmpty(forminst.getVariables())) {
    					vars = JSONObject.parseObject(forminst.getVariables(), Map.class);
    				}
    				vars.putAll(viewVariables);
    				forminst.setVariables(JSON.toJSONString(vars));    				
    				forminstService.save(forminst);
    			}    			
    		}
    	}
    	
		// resolveTask() ?????? claim() ??????????????? act_hi_taskinst ?????? assignee ???????????? null
		taskService.resolveTask(taskId, instanceVariables);
        // ?????????????????????act_hi_taskinst ?????? assignee ??????????????? null
        taskService.claim(taskId, assignee);
    	
    	if(StringUtils.isNotEmpty(model.getOutcome())) {
    		instanceVariables.put(ActivitiConstant.KEY_OUTCOME,  model.getOutcome());    		
    	}
    	logger.info("instanceVariables:{}", instanceVariables.toString());
    	if(model.getTargetRef() != null && model.getTargetRef().length > 0) {// ?????????????????????????????????????????????
    		int len = model.getTargetRef().length;
    		for (int i = 0; i < len; i++) {
    			instanceVariables.put(model.getTargetRef()[i] + ActivitiConstant.VAR_TAG_ASSIGNEE, 
    					model.getTargetAssignee()[i]);
			}
    	}
    	// ????????????
    	taskService.complete(taskId, instanceVariables);
    	
    	// ??????????????????????????????
    	if(task.getParentTaskId() == null) {// ??????????????????????????????
    		forminstService.updateNodeName(processInstanceId, task.getExecutionId());
    	}
    	
    }


    /**
     * ????????????
     * @param files
     * @param taskId
     * @param processDefinitionId
     * @param processInstanceId
     */
	private void upload(MultipartFile[] files, String taskId, String processDefinitionId, String processInstanceId) {
		if(files == null) {
			return;
		}
		// ????????????
    	for (MultipartFile file : files) {
			try {
				boolean support = AttachmentUtil.supportFileType(file.getInputStream(), ossService.getProperties().getSupportFileTypes());
				if(!support) {
					throw new RuntimeException("??????????????????????????????????????????");
				}    		
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
    	}    	
    	List<Attachment> attachList = new ArrayList<Attachment>();
    	for (MultipartFile file : files) {
			try {
				String originalName = file.getOriginalFilename();
				String targetName = ossService.upload(file.getInputStream(), originalName);
				Attachment attach = new Attachment();
				attach.setOriginalName(originalName);
				attach.setTargetName(targetName);
				attach.setProcessDefinitionId(processDefinitionId);
				attach.setProcessInstanceId(processInstanceId);
				attach.setTaskId(taskId);
				attach.setCreateTime(new Date());
				attachList.add(attach);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
    	// ????????????
    	if(!attachList.isEmpty()) {
    		attachmentService.insertBatch(attachList);
    	}
	}
    
    @Override
    public void saveStartableUsers(DefinitionStartableRequest startable) {
    	String processDefinitionId = startable.getId();
    	List<IdentityLink> list = repositoryService.getIdentityLinksForProcessDefinition(processDefinitionId);
    	for (IdentityLink identityLink : list) {
    		if(StringUtils.isNotEmpty(identityLink.getUserId())) {
    			repositoryService.deleteCandidateStarterUser(processDefinitionId, identityLink.getUserId());
    		}
    		if(StringUtils.isNotEmpty(identityLink.getGroupId())) {
    			repositoryService.deleteCandidateStarterGroup(processDefinitionId, identityLink.getGroupId());
    		}
		}
    	
    	String[] users = startable.getUsers();
    	for (String userId : users) {
    		repositoryService.addCandidateStarterUser(processDefinitionId, userId);
		}
    	
    	String[] groups = startable.getGroups();
    	for (String groupId : groups) {
    		repositoryService.addCandidateStarterGroup(processDefinitionId, groupId);
		}
    	
    }
    
    @Override
    public List<HistoricActivityResponse> queryHisActivities(HistoricActivityResponse historicActivity) {
    	return actHistoryService.queryHisActivities(historicActivity);
    }
    
    
    @Override
    public void stopProcess(ProcessStopRequest model) {
    	String username = CommonUtil.getCurrentUsername();
    	String processInstanceId = model.getProcessInstanceId();
    	// ????????????
    	identityService.setAuthenticatedUserId(username);
    	List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    	for (Task task : tasks) {
    		taskService.addComment(task.getId(), model.getProcessInstanceId(), model.getStopReason());
		}
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("processInstanceId", processInstanceId);
    	params.put("nodeName", ForminstNode.STOPED.getDesc());
    	forminstService.updateNodeName(params);    	
    	// ????????????
    	runtimeService.deleteProcessInstance(processInstanceId, model.getStopReason());
    }
    
    /**
     * activiti???8???service?????????activiti??????????????????????????????ProcessEngine???????????????
		TaskService-????????????????????????????????????
		RepositoryService-???activiti????????????????????????????????????????????????
		RuntimeService-??????????????????????????????????????????????????????????????????????????????????????????
		IdentityService-?????????????????????????????????????????????????????????????????????
		HistoryService-????????????????????????????????????????????????
		FormService-?????????????????????????????????
		DynamicBpmnService-??????????????????????????????????????????
		ManagementService-???????????????????????????activiti?????????????????????????????????????????????????????????????????????
     */
   
    
    /** 
     * ?????????????????????????????? 
     * 
     * @param context 
     * @param request 
     * @param user 
     * @return 
     */  
//    public void rollbackFirstTask(String taskId, String user) {  
//        // ????????????REJECT???status  
//        taskService.removeVariable(taskId, "status");  
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();  
//        // ????????????  
//        managementService.executeCommand(new TaskDeleteCmd(taskId));  
//        // ????????????  
//        managementService.executeCommand(new ExecutionVariableDeleteCmd(task.getExecutionId()));  
//        // ?????????????????????????????????  
//        managementService.executeCommand(new FlowToFirstCmd(task));  
//        //return this.taskResponse(task.getProcessInstanceId());
//    } 
    
    /**
     * ?????????????????????????????? 
     * @param taskId
     * @param user
     */
//    public void backwardTask(String taskId, String user) {  
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();  
//        // ????????????  
//        managementService.executeCommand(new ExecutionVariableDeleteCmd(task.getExecutionId())); 
//        // ???????????????????????????
//        managementService.executeCommand(new FlowToPreNodeCmd(task));  
//        // ????????????
//        managementService.executeCommand(new TaskDeleteCmd(taskId));  
//       // return this.taskResponse(task,task.getProcessInstanceId());  
//    }
    
}
