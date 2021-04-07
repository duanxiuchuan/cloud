package com.erongdu.server.activiti.config;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.erongdu.server.activiti.listener.GlobalEventListener;

/**
 * Activiti配置扩展
 * @Author lh@erongdu.com
 * @date 2020-7-23
 */
@Configuration
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer{
    
	private static final Logger log = LoggerFactory.getLogger(ActivitiConfig.class);
	
	@Autowired GlobalEventListener globalEventListener;
	
    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
    	// 解決工作流生成图片乱码问题
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        
        // 添加全局监听器（支持多个）
        List<ActivitiEventListener> eventListeners =new ArrayList<ActivitiEventListener>();
        eventListeners.add(globalEventListener);        
        processEngineConfiguration.setEventListeners(eventListeners);
    }
    
    /**
     * 仅用于输出流程定义数量和任务数，生产环境可以去除
     * @param repositoryService
     * @param runtimeService
     * @param taskService
     * @return
     */
	@Bean
	public CommandLineRunner init(final RepositoryService repositoryService, final RuntimeService runtimeService,
			final TaskService taskService) {
		return new CommandLineRunner() {
			@Override
			public void run(String... strings) throws Exception {

				log.info("Number of process definitions : {}",
						repositoryService.createProcessDefinitionQuery().count());
				log.info("Number of tasks : {}", taskService.createTaskQuery().count());
			}
		};

	}
}
