package com.erongdu.server.activiti.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.erongdu.common.core.entity.activiti.Attachment;
import com.erongdu.server.activiti.dao.AttachmentDao;
import com.erongdu.server.activiti.service.AttachmentService;

/**
 * 
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-6
 */
 
@Service("attachmentService") 
public class AttachmentServiceImpl implements AttachmentService{
	
    @Resource
    private AttachmentDao attachmentDao;

    
    @Override
	public void insertBatch(List<Attachment> list) {
    	attachmentDao.insertBatch(list);
	}
	@Override
	public int insert(Attachment entity) {
		return attachmentDao.insert(entity);
	}

	@Override
	public int delete(Long id) {
		return attachmentDao.deleteById(id);
	}

	@Override
	public int update(Attachment entity) {
		return attachmentDao.update(entity);
	}
	
	@Override
	public List<Attachment> list(Map<String, Object> params) {
		return attachmentDao.listSelective(params);
	}

	@Override
	public Attachment findById(Long id) {
		return attachmentDao.findById(id);
	}
}