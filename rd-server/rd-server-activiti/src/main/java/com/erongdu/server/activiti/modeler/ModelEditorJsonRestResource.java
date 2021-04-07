/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.erongdu.server.activiti.modeler;

import java.nio.charset.StandardCharsets;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Tijs Rademakers
 */
@RestController
public class ModelEditorJsonRestResource implements ModelDataJsonConstants {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);
	public static final String MODEL_CATEGORY = "category";
	public static final String MODEL_FORM_KEY = "formKey";
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private ObjectMapper objectMapper;

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/actdesign/model/{modelId}/json", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getEditorJson(@PathVariable String modelId) {
		ObjectNode modelNode = null;
		Model model = repositoryService.getModel(modelId);
		if (model != null) {
			try {
				if (StringUtils.isNotEmpty(model.getMetaInfo())) {
					modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
				} else {
					modelNode = objectMapper.createObjectNode();
					modelNode.put(MODEL_NAME, model.getName());
					modelNode.put(MODEL_CATEGORY, model.getCategory());
				}
				modelNode.put(MODEL_ID, model.getId());
				ObjectNode editorJsonNode = (ObjectNode) objectMapper
						.readTree(new String(repositoryService.getModelEditorSource(model.getId()), StandardCharsets.UTF_8));
				modelNode.put("model", editorJsonNode);

			} catch (Exception e) {
				LOGGER.error("Error creating model JSON", e);
				throw new ActivitiException("Error creating model JSON", e);
			}
		}
		return modelNode;
	}
	
	/**
     * 根据生成的ID获取模型流程编辑器
     * @param modelId
     * @return
     */
	@RequestMapping(value = "/actdesign/model/{modelId}/xml", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getEditorXml(@PathVariable String modelId) {
        JSONObject jsonObject = null;
        Model model = repositoryService.getModel(modelId);
        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    jsonObject = JSON.parseObject(model.getMetaInfo());
                } else {
                    jsonObject = new JSONObject();
                    jsonObject.put(MODEL_NAME, model.getName());
                    jsonObject.put(MODEL_CATEGORY, model.getCategory());
                }
                String bpmnXml = "";
                jsonObject.put(MODEL_ID, model.getId());
                //先查询是否有原样xml,有则直接返回页面
                byte[] bs = repositoryService.getModelEditorSourceExtra(model.getId());
                if(bs == null) {
                	// 原样xml不存在则查json文件
                	bs = repositoryService.getModelEditorSource(model.getId());
                	if(bs != null) {
                		BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
                		JsonNode editorNode = new ObjectMapper().readTree(bs);
                		BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
                		// 如果没有Processes,认为是一个空流程
                		if (!bpmnModel.getProcesses().isEmpty()) {
                			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
                			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
                			bpmnXml = new String(bpmnBytes);
                		}
                	}
                }else {
                	bpmnXml = new String(bs, StandardCharsets.UTF_8);                	
                }    
                jsonObject.put("bpmnXml", bpmnXml);
            } catch (Exception e) {
            	LOGGER.error("创建model的json串失败", e);
                throw new ActivitiException("无法读取model信息", e);
            }
        } else {
        	LOGGER.error("创建model的json串失败[{}]", modelId);
            throw new ActivitiException("未找到对应模型信息");
        }
        return jsonObject;
    }
	
	
}
