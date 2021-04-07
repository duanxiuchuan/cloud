package com.erongdu.server.activiti.modeler;

import static com.erongdu.server.activiti.modeler.ModelEditorJsonRestResource.*;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ModelEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erongdu.common.core.base.BaseController;
import com.erongdu.server.activiti.model.request.ModelRequest;
import com.erongdu.server.activiti.util.PageableUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/api")
public class ModelerController extends BaseController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);
    private static final String PREFIX = "modeler";

    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping("/modeler/modelList")
    public String modelList(HttpServletRequest request) {
        return PREFIX + "/modelList";
    }

    @PostMapping("/modeler/list")
    @ResponseBody
    public Object list(ModelEntityImpl modelEntity, Pageable pageable) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        modelQuery.orderByLastUpdateTime().desc();
        // 条件过滤
        if (StringUtils.isNotBlank(modelEntity.getKey())) {
            modelQuery.modelKey(modelEntity.getKey());
        }
        if (StringUtils.isNotBlank(modelEntity.getName())) {
            modelQuery.modelNameLike("%" + modelEntity.getName() + "%");
        }
        long count = modelQuery.count();
        int firstResult = PageableUtil.offset(pageable);
        List<Model> resultList = modelQuery.listPage(firstResult, pageable.getPageSize());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("content", resultList);
        data.put("totalElements", count);
        return success(data);
    }

    @GetMapping("/modeler/addModal")
    public String addModal() {
        return PREFIX + "/modelModal";
    }

    /**
     * 创建模型
     */
    @SuppressWarnings("deprecation")
	@RequestMapping(value = "/modeler/create")
    @ResponseBody
    public Object create(ModelRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(MODEL_NAME, request.getName());
            modelObjectNode.put(MODEL_REVISION, 1);
            modelObjectNode.put(MODEL_CATEGORY, request.getCategory());
            modelObjectNode.put(MODEL_FORM_KEY, request.getFormKey());
            String description = StringUtils.defaultString(request.getDescription());
            modelObjectNode.put(MODEL_DESCRIPTION, description);

            Model newModel = repositoryService.newModel();
            newModel.setMetaInfo(modelObjectNode.toString());
            newModel.setName(request.getName());
            newModel.setKey(StringUtils.defaultString(request.getKey()));
            newModel.setCategory(request.getCategory());

            repositoryService.saveModel(newModel);
            repositoryService.addModelEditorSource(newModel.getId(), editorNode.toString().getBytes("utf-8"));
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("modelId", newModel.getId());
            return success(data);
        } catch (Exception e) {
        	LOGGER.error("创建模型失败：", e);
        }
        return fail();
    }

    /**
     * 根据Model部署流程
     */
    @RequestMapping(value = "/modeler/deploy/{modelId}")
    @ResponseBody
    public Object deploy(@PathVariable("modelId") String modelId, RedirectAttributes redirectAttributes) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment()
            		.category(modelData.getCategory())
            		.name(modelData.getName()).addString(processName, new String(bpmnBytes, "UTF-8")).deploy();
            LOGGER.info("部署成功，部署ID=" + deployment.getId());
            return success("部署成功");
        } catch (Exception e) {
            LOGGER.error("根据模型部署流程失败：modelId={}", modelId, e);

        }
        return fail("部署失败");
    }

    /**
     * 导出model的xml文件
     */
    @RequestMapping(value = "/modeler/export/{modelId}")
    public void export(@PathVariable("modelId") String modelId, HttpServletResponse response) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            byte[] bs = repositoryService.getModelEditorSource(modelData.getId());
            if(bs == null) {
            	return;
            }
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(bs);
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

            // 流程非空判断
            if (!CollectionUtils.isEmpty(bpmnModel.getProcesses())) {
                BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
                byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
                ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
                String filename = bpmnModel.getMainProcess().getId() + ".bpmn";
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                IOUtils.copy(in, response.getOutputStream());
                response.flushBuffer();
            }

        } catch (Exception e) {
            LOGGER.error("导出model的xml文件失败：modelId={}", modelId, e);
            
        }
    }

    @PostMapping("/modeler/remove")
    @ResponseBody
    public Object remove(String ids) {
        try {
            repositoryService.deleteModel(ids);
            return success();
        }
        catch (Exception e) {
            return fail(e.getMessage());
        }
    }

}
