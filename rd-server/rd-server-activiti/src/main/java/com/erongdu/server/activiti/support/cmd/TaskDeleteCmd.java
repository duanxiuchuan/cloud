package com.erongdu.server.activiti.support.cmd;
import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManagerImpl;  
/** 
 *  删除任务
 * @author lh@erongdu.com
 * @since 2020-07-17
 *
 */
public class TaskDeleteCmd extends NeedsActiveTaskCmd<String> {  
	
	private static final long serialVersionUID = 1L;
	public TaskDeleteCmd(String taskId) {  
        super(taskId);  
    }  
    @Override  
    public String execute(CommandContext commandContext, TaskEntity currentTask) {  
        TaskEntityManagerImpl taskEntityManager = (TaskEntityManagerImpl) commandContext.getTaskEntityManager();  
        ExecutionEntity executionEntity = currentTask.getExecution();  
        taskEntityManager.deleteTask(currentTask, "reject", false, false);  
        return executionEntity.getId();  
    }  
} 