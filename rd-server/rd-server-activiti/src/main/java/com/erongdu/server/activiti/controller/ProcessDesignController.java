package com.erongdu.server.activiti.controller;

import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.base.BaseController;
import com.erongdu.server.activiti.model.request.ModelRequest;
import com.erongdu.server.activiti.model.request.ProcessDefinitionRequest;
import com.erongdu.server.activiti.model.request.ProcessSuspendRequest;
import com.erongdu.server.activiti.service.ProcessDesignService;

/**
 * @author lh@erongdu.com
 * @since 2020-07-23
 */
@RestController
@RequestMapping("/actdesign")
public class ProcessDesignController extends BaseController{
    
    @Autowired
    private ProcessDesignService processDesignService;
    
    /**
     * 创建模型
     */
    @RequestMapping(value = "/model/insert", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void createModel(@RequestBody  ModelRequest model) throws UnsupportedEncodingException {
        processDesignService.createModel(model);
    }
    
    /**
     * 模型列表
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/model/list", method = RequestMethod.GET)
    public Object listModel(Pageable pageable) {
        return success(processDesignService.listModel(pageable));
    }
    
    
//    /**
//     * 保存模型
//     */
//    @RequestMapping(value = "/model/{modelId}/xml/save", method = RequestMethod.POST, produces = "application/json")
//    @ResponseStatus(value = HttpStatus.OK)
//    public void saveModelXml(@PathVariable String modelId, @RequestBody ModelXmlRequest xml) {
//        modelSaveRestResource.saveModelXml(modelId, xml);
//    }
//    
//    /**
//     * 根据生成的ID获取模型流程编辑器
//     * @param modelId
//     * @return
//     */
//    @RequestMapping(value = "/model/{modelId}/xml", method = RequestMethod.GET, produces = "application/json")
//    @ResponseStatus(value = HttpStatus.OK)
//    public JSONObject getEditorXml(@PathVariable String modelId) {
//        return modelEditorJsonRestResource.getEditorXml(modelId);
//    }
    
    /**
     * 删除模型
     * @param modelId
     */
    @ResponseBody
    @GetMapping(value = "/deleteModel")
    public void flowDelete(@RequestParam(name = "modelId") String modelId){
        processDesignService.deleteModel(modelId);
    }
    
    
    /**
     * 流程模型部署
     * @param modelId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/model/deploy")
    public Object deploy(@RequestParam(name = "modelId") String modelId) throws Exception {
        String result = processDesignService.deployModel(modelId);
        if("success".contentEquals(result)) {
        	return success();
        }else {
        	return badRequest(result);
        }
    }
    
    /**
     * 流程定义状态变更
     * @param id	流程定义id
     * @param suspendState	1-激活；2 -暂停
     * @return
     */
    @PostMapping( "/updateState")
    @ResponseBody
    public Object updateState(@RequestBody ProcessSuspendRequest suspend) {
    	processDesignService.updateState(suspend);
        return success();
    }
    
    /**
     * 删除流程部署
     * @param deploymentId
     * @return
     */
    @PostMapping("/deleteDeployment")
    @ResponseBody
    public Object deleteDeployment(@RequestBody ProcessDefinitionRequest model) {
    	processDesignService.deleteDeployment(model.getDeploymentId());
    	return success();
    }
    
    /**
     * 转换流程定义为模型
     * @param processDefinitionId
     * @return
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @PostMapping(value = "/convert2Model")
    @ResponseBody
    public Object convertToModel(@RequestBody ProcessDefinitionRequest model) {
        processDesignService.convertToModel(model.getProcessDefinitionId());
        return success();
    }
}
