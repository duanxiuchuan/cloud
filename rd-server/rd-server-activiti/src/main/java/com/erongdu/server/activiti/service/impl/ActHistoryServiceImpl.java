package com.erongdu.server.activiti.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Comment;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erongdu.server.activiti.model.response.HistoricActivityResponse;
import com.erongdu.server.activiti.service.ActHistoryService;

@Service
public class ActHistoryServiceImpl implements ActHistoryService {

	@Autowired
	private HistoryService historyService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	
	
	/**
	 * 任务是否已归档
	 * @param processInstanceId
	 * @return
	 */
	@Override
	public boolean finished(String processInstanceId) {
		return historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).unfinished().count() == 0;
	}

	/**
	 * 最新的用户任务
	 * @param executionId
	 * @return
	 */
	@Override
	public HistoricActivityInstance nextUserActivity(String executionId) {
		List<HistoricActivityInstance> items = historyService.createHistoricActivityInstanceQuery()
				.executionId(executionId).activityType("userTask").unfinished()
				.orderByHistoricActivityInstanceStartTime().desc()
				.list();
		return (items == null || items.size() ==0) ? null : items.get(0);
	}

	/**
	 * 查询审批记录
	 * @param processInstanceId
	 * @param historicActivity
	 * @return
	 */
	@Override
    public List<HistoricActivityResponse> queryHisActivities(HistoricActivityResponse historicActivity) {
        List<HistoricActivityResponse> activityList = new ArrayList<HistoricActivityResponse>();
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery();
        if (StringUtils.isNotBlank(historicActivity.getAssignee())) {
            query.taskAssignee(historicActivity.getAssignee());
        }
        if (StringUtils.isNotBlank(historicActivity.getActivityName())) {
            query.activityName(historicActivity.getActivityName());
        }
        List<HistoricActivityInstance> list = query.processInstanceId(historicActivity.getProcessInstanceId())
                .activityType("userTask")
                //.finished()
                .orderByHistoricActivityInstanceStartTime()
                .desc()
                .list();
        list.forEach(instance -> {
        	HistoricActivityResponse activity = new HistoricActivityResponse();
            BeanUtils.copyProperties(instance, activity);
            String taskId = instance.getTaskId();
            List<Comment> comment = taskService.getTaskComments(taskId, "comment");
            if (!CollectionUtils.isEmpty(comment)) {
                activity.setComment(comment.get(0).getFullMessage());
            }
            
            User user = identityService.createUserQuery().userId(instance.getAssignee()).singleResult();
            if (user != null) {
                activity.setAssigneeName(StringUtils.isEmpty(user.getFirstName())? instance.getAssignee() : user.getFirstName());
            }
            activityList.add(activity);
        });
        return activityList;
    }
}
