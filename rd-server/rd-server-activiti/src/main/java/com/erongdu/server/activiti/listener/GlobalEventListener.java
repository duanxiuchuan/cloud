package com.erongdu.server.activiti.listener;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.erongdu.server.activiti.listener.event.EventHandler;
import com.erongdu.server.activiti.listener.event.TaskCreatedEventHandler;

@Component
public class GlobalEventListener implements ActivitiEventListener {
	
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired 
    private TaskCreatedEventHandler taskCreatedEventHandler;
    
    /**
     * 各类 Event 的处理器
     */
    private Map<ActivitiEventType, EventHandler> handlers = new HashMap<ActivitiEventType, EventHandler>();
    
    @PostConstruct
    public void init() {
    	handlers.put(ActivitiEventType.TASK_CREATED, taskCreatedEventHandler);
    }
    @Override
    public void onEvent(ActivitiEvent event) {
        EventHandler eventHandler = handlers.get(event.getType());
        if(eventHandler!=null){
            eventHandler.handle(event);
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    public Map<ActivitiEventType, EventHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(Map<ActivitiEventType, EventHandler> handlers) {
        this.handlers = handlers;
    }
}