package com.erongdu.server.activiti.model.response;

import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntityImpl;
/**
 * 审批历史记录
 * @author lh@erongdu.com
 * @since  2020年7月31日
 *
 */
public class HistoricActivityResponse extends HistoricActivityInstanceEntityImpl {

	private static final long serialVersionUID = 1L;

	/** 审批批注 */
    private String comment;

    /** 办理人姓名 */
    private String assigneeName;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

}
