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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.server.activiti.model.request.ModelXmlRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Tijs Rademakers
 */
@RestController
public class ModelSaveRestResource implements ModelDataJsonConstants {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ModelSaveRestResource.class);

	@Autowired
	private RepositoryService repositoryService;


	/**
	 * 保存模型-xml格式
	 */
	@RequestMapping(value = "/actdesign/model/{modelId}/xml/save", method = RequestMethod.POST)
	public void saveModelXml(@PathVariable("modelId") String modelId, @RequestBody ModelXmlRequest xml) {
		try {
			String bpmnXml = converterXmlToJson(xml.getBpmn_xml()).toString();
			repositoryService.addModelEditorSource(modelId, bpmnXml.getBytes(StandardCharsets.UTF_8));
			//repositoryService.addModelEditorSourceExtra(modelId, xml.getSvg_xml().getBytes(StandardCharsets.UTF_8));
			// 将页面xml原样保存为扩展资源，读取时先查扩展资源
			repositoryService.addModelEditorSourceExtra(modelId, xml.getBpmn_xml().getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			LOGGER.error("Error saving model", e);
			throw new ActivitiException("Error saving model", e);
		}
	}

	public static JsonNode converterXmlToJson(String xml) {
		InputStream bpmnStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
		XMLInputFactory xif = XMLInputFactory.newInstance();
		XMLStreamReader xtr = null;
		try {
			InputStreamReader in = new InputStreamReader(bpmnStream, StandardCharsets.UTF_8);
			xtr = xif.createXMLStreamReader(in);
		} catch (Exception e) {
			LOGGER.error("Error converterXmlToJson", e);
		}
		BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
		BpmnJsonConverter converter = new BpmnJsonConverter();
		ObjectNode modelNode = converter.convertToJson(bpmnModel);
		return modelNode;
	}

}
