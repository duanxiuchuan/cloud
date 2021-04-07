package com.erongdu.server.activiti.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erongdu.common.core.base.BaseController;
import com.erongdu.common.core.entity.activiti.Attachment;
import com.erongdu.common.oss.service.OssService;
import com.erongdu.server.activiti.model.request.AttachmentRequest;
import com.erongdu.server.activiti.service.AttachmentService;
import com.erongdu.server.activiti.util.AttachmentUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lh@erongdu.com
 * @version 4.1.0
 * @date 2020-8-6
 */
@Slf4j
@RestController
@RequestMapping(value = "/actattach")
public class ActAttachmentController extends BaseController{
    @Resource
    private AttachmentService attachmentService;
    @Resource
    private OssService ossService;
    
    /**
     * 文件下载
     * @param key
     * @param response
     * @throws Exception
     */
    @GetMapping("/download/{key}")
    public void download(@PathVariable(value = "key") String key, HttpServletResponse response)throws Exception {
    	OutputStream out = null;
    	try {
    		String source = key;
    		if(StringUtils.isNumeric(key)) {
    			Attachment attachment = attachmentService.findById(Long.valueOf(key));
    			if(attachment == null || StringUtils.isEmpty(attachment.getTargetName())) {
    				return;
    			}
    			source = attachment.getTargetName();
    		}
    		// 读出文件到i/o流
    		response.setHeader("Pragma", "no-cache");
    		response.setHeader("Cache-Control", "no-cache");
    		response.setDateHeader("Expires", 0);
    		response.reset();
    		// 设置response的编码方式
    		response.setContentType("application/octet-stream");
    		// 发送到客户端
    		String filename = new String(source.getBytes(), "ISO-8859-1");
    		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
    		
    		// inputstream
    		InputStream in = ossService.download(source);
//    		String bucketName = ossService.getProperties().getBucketName();
//    		MinioClient client = new MinioUtil().getClient();
//    		InputStream in = client.getObject(bucketName, source);
    		
    		// outputstram
    		out = new BufferedOutputStream(response.getOutputStream());
    		//创建一个缓冲区
    		byte buffer[] = new byte[1024];
    		int len = 0;
    		while ((len=in.read(buffer))>0){
    			out.write(buffer,0,len);
    		}
    		in.close();
    		out.flush();
    		
    	}catch(Exception e) {
    		log.error(e.getMessage(), e);
    	}finally {
    		if(out != null) {
    			out.close();
    		}
    	}		
	}
    

    /**
     * 附件上传
     * @param file
     * @param taskId
     * @param processDefinitionId
     * @param processInstanceId
     */
    @PostMapping("/upload")
	public Object upload(AttachmentRequest request) {
		MultipartFile[] files = request.getFile();
		if(files == null) {
			return fail("请上传文件");
		}
		String supportFileTypes = ossService.getProperties().getSupportFileTypes();
		// 上传附件
    	for (MultipartFile file : files) {
			try {
				boolean support = AttachmentUtil.supportFileType(file.getInputStream(), supportFileTypes);
				if(!support) {
					throw new RuntimeException("该文件格式不支持，请重新上传");
				}    		
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
    	}    	
    	String processDefinitionId = request.getProcessDefinitionId();
    	String processInstanceId = request.getProcessInstanceId();
    	String taskId = request.getTaskId();
    	List<Attachment> attachList = new ArrayList<Attachment>();
    	for (MultipartFile file : files) {
			try {
				String originalName = file.getOriginalFilename();
				String targetName = ossService.upload(file.getInputStream(), originalName);
				Attachment attach = new Attachment();
				attach.setOriginalName(originalName);
				attach.setTargetName(targetName);
				attach.setProcessDefinitionId(processDefinitionId);
				attach.setProcessInstanceId(processInstanceId);
				attach.setTaskId(taskId);
				attach.setCreateTime(new Date());
				attachList.add(attach);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
    	// 批量入库
    	if(!attachList.isEmpty()) {
    		for (Attachment attachment : attachList) {
    			attachmentService.insert(attachment);
    			attachment.setUrl("/actattach/download/"+attachment.getId());
			}
    	}
    	return success(attachList);
	}
    
    /**
     * 附件删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Object delete(@PathVariable(value = "id") Long id) {
    	Attachment attachment = attachmentService.findById(id);
		try {
			ossService.remove(attachment.getTargetName());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
    	attachmentService.delete(id);    	
    	return success();
    }
    
    

}
