package com.erongdu.server.activiti.listener.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
/**
 * 事件处理器
 * @author lh@erongdu.com
 * @since 2020年8月21日
 * @version 4.1.0
 */
public interface EventHandler {
	
    void handle(ActivitiEvent event);
    
}