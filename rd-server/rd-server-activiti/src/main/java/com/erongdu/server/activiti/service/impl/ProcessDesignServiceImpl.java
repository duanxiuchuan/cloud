package com.erongdu.server.activiti.service.impl;

import static com.erongdu.server.activiti.modeler.ModelEditorJsonRestResource.MODEL_CATEGORY;
import static com.erongdu.server.activiti.modeler.ModelEditorJsonRestResource.MODEL_FORM_KEY;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_DESCRIPTION;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_NAME;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_REVISION;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.erongdu.common.core.entity.activiti.ProcdefForm;
import com.erongdu.common.core.enums.SuspensionState;
import com.erongdu.server.activiti.dao.ProcdefFormDao;
import com.erongdu.server.activiti.model.request.ModelRequest;
import com.erongdu.server.activiti.model.request.ProcessSuspendRequest;
import com.erongdu.server.activiti.model.response.ModelResponse;
import com.erongdu.server.activiti.service.ProcessDesignService;
import com.erongdu.server.activiti.util.PageableUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @Author lh@erongdu.com
 * @date 2020/7/21
 */
@Service
public class ProcessDesignServiceImpl implements ProcessDesignService {
    
	private static final Logger logger = LoggerFactory.getLogger(ProcessDesignServiceImpl.class);
	
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProcdefFormDao procdefFormDao;
    @Autowired
    private HistoryService historyService;
    
    
    /**
     * ????????????
     * @param key
     * @param name
     * @param category
     * @param description
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("deprecation")
	@Override
    public void createModel(ModelRequest mr) throws UnsupportedEncodingException{
    	
    	String key = mr.getKey();
    	if(StringUtils.isEmpty(key)) {
    		key = mr.getFormKey();
    	}
    	//????????????????????????
    	Model model = repositoryService.newModel();
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(MODEL_NAME, mr.getName());
        modelNode.put(MODEL_CATEGORY, mr.getCategory());
        modelNode.put(MODEL_DESCRIPTION, mr.getDescription());
        modelNode.put(MODEL_FORM_KEY, mr.getFormKey());
    
        model.setName(mr.getName());
        model.setKey(key);
        model.setCategory(mr.getCategory());
        model.setMetaInfo(modelNode.toString());
    
        repositoryService.saveModel(model);
        String id = model.getId();
    
        //??????ModelEditorSource
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", key);
        editorNode.put("resourceId", key);
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://activiti.org/bpmn");
        editorNode.put("stencilset", stencilSetNode);
    
        repositoryService.addModelEditorSource(id, editorNode.toString().getBytes("utf-8"));
    }
    
    /**
     * ????????????
     * @return
     */
    @Override
    public Map<String, Object>  listModel(Pageable pageable) {
    	ModelQuery query = repositoryService.createModelQuery();
    	long count = query.count();
    	int firstResult = PageableUtil.offset(pageable);
    	List<Model> list = query.listPage(firstResult, pageable.getPageSize());
    	List<ModelResponse> data = new ArrayList<ModelResponse>();
    	for (Model model : list) {
    		ModelResponse mr = new ModelResponse();
    		BeanUtils.copyProperties(model, mr);
    		if(StringUtils.isNotEmpty(mr.getMetaInfo())) {
    			JSONObject metaInfo = JSONObject.parseObject(mr.getMetaInfo());
    			mr.setFormKey(metaInfo.getString("formKey"));
    			mr.setDescription(metaInfo.getString("description"));
    		}    	
    		data.add(mr);
		}    	
    	return PageableUtil.toPage(data, count);
    }
    
    /**
     * ????????????
     * @param modelId
     */
    @Override
    public void deleteModel(String modelId) {
        repositoryService.deleteModel(modelId);
    }
    
    /**
     * ????????????
     * @param modelId
     */
    @Override
    public String deployModel(String modelId) throws Exception {
        // ????????????
        Model modelData = repositoryService.getModel(modelId);
        byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
        if(null == bytes) {
            return "???????????????????????????????????????????????????????????????????????????";
        }
        JsonNode modelNode = objectMapper.readTree(bytes);
        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        if (model.getProcesses().size() == 0){
            return "??????????????????????????????????????????????????????????????????";
        }
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
    
        //????????????
        String processName = modelData.getName() + ".bpmn";
        Deployment deployment = repositoryService.createDeployment()
                .name(modelData.getName())
                .key(modelData.getKey())
                .category(modelData.getCategory())
                .addString(processName, new String(bpmnBytes, "UTF-8"))
                .deploy();
        modelData.setDeploymentId(deployment.getId());
        repositoryService.saveModel(modelData);
        
        //?????????????????????????????????
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).latestVersion().singleResult();
		String processDefinitionId = definition.getId();		
		//?????????????????????????????????
		repositoryService.setProcessDefinitionCategory(processDefinitionId, modelData.getCategory());
		
		if(StringUtils.isNotEmpty(modelData.getMetaInfo())) {
			JSONObject metaInfo = JSONObject.parseObject(modelData.getMetaInfo());
			// ??????form??????
			if(metaInfo.containsKey(MODEL_FORM_KEY)) {
				String formKey = metaInfo.getString(MODEL_FORM_KEY);
				ProcdefForm pf = new ProcdefForm(processDefinitionId, formKey);
				procdefFormDao.insert(pf);	
			}			
		}		
        return "success";
    }
    
    /**
     * ???????????????model
     */
    @Override
    public void convertToModel(String processDefinitionId) {
    	
    	ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        XMLStreamReader xtr = null;
		try {
			InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
			xtr = xif.createXMLStreamReader(in);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} catch (XMLStreamException e) {
			logger.error(e.getMessage(), e);
		}
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectNode modelNode = converter.convertToJson(bpmnModel);
        Model modelData = repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getName());
        modelData.setCategory(processDefinition.getCategory());
        modelData.setDeploymentId(processDefinition.getDeploymentId());
        
        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(MODEL_REVISION, 1);
        modelObjectNode.put(MODEL_DESCRIPTION, processDefinition.getDescription());
        modelObjectNode.put(MODEL_CATEGORY, processDefinition.getCategory());   
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("processDefinitionId", processDefinitionId);
        ProcdefForm pform = procdefFormDao.findSelective(params);
        if(pform != null) {
        	modelObjectNode.put(MODEL_FORM_KEY, pform.getFormKey());
        }
        modelData.setMetaInfo(modelObjectNode.toString());

        repositoryService.saveModel(modelData);
        try {
			repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
    }
    
    @Override
    public void updateState(ProcessSuspendRequest suspend) {
    	String processDefinitionId = suspend.getId();
    	int suspendState = suspend.getSuspensionState();
    	// ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    	// ?????????????????????????????????????????????????????????????????????????????????
    	// ???????????????act_re_procdef ??? SUSPENSION_STATE_ ??? 2
    	switch (SuspensionState.of(suspendState)) {
	    	case ACTIVE:
	    		repositoryService.activateProcessDefinitionById(processDefinitionId);
	    		break;
			case SUSPEND:
				repositoryService.suspendProcessDefinitionById(processDefinitionId);
				break;
			default:
				break;
		}
    }
    

    public void deleteDeployment(String deploymentId) {
    	
    	long count = historyService.createHistoricProcessInstanceQuery().deploymentId(deploymentId).count();
    	if(count > 0) {
    		throw new RuntimeException("????????????????????????????????????????????????");
    	}
    	
    	repositoryService.deleteDeployment(deploymentId, true); // true ????????????????????????????????? act_ru_execution ??????
    }
}
