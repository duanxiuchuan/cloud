package com.erongdu.server.activiti.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.erongdu.server.activiti.model.request.ModelRequest;
import com.erongdu.server.activiti.model.request.ProcessSuspendRequest;

/**
 * @author lh@erongdu.com
 * @since 2020-07-23
 */
public interface ProcessDesignService {
    
    void createModel(ModelRequest model) throws UnsupportedEncodingException;
    
    Map<String, Object> listModel(Pageable pageable);
    
    void deleteModel(String modelId);
    
    String deployModel(String modelId) throws Exception;
    
    /**
     * 状态变更
     * @param processDefinitionId
     * @param suspendState
     */
    void updateState(ProcessSuspendRequest suspend);
    
    /**
     * 删除流程定义
     * @param deploymentId
     */
    void deleteDeployment(String deploymentId);
    
    void convertToModel(String processDefinitionId);
}
