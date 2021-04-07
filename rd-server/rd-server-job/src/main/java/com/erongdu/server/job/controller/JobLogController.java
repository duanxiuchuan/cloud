package com.erongdu.server.job.controller;

import com.erongdu.common.core.entity.CommonResponse;
import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.common.core.entity.constant.StringConstant;
import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.common.core.utils.SpringContextUtil;
import com.erongdu.server.job.entity.JobLog;
import com.erongdu.server.job.service.IJobLogService;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author erongdu.com
 */
@Slf4j
@Validated
@RestController
@RequestMapping("log")
@RequiredArgsConstructor
public class JobLogController {

    private final IJobLogService jobLogService;

    @GetMapping("count")
    public CommonResponse test() {
    	IJobLogService service = SpringContextUtil.getBean(IJobLogService.class);
    	return new CommonResponse().data(service.count());
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('job:log:view')")
    public CommonResponse jobLogList(QueryRequest request, JobLog log) {
        Map<String, Object> dataTable = CommonUtil.getDataTable(this.jobLogService.findJobLogs(request, log));
        return new CommonResponse().data(dataTable);
    }

    @DeleteMapping("{jobIds}")
    @PreAuthorize("hasAuthority('job:log:delete')")
    public void deleteJobLog(@NotBlank(message = "{required}") @PathVariable String jobIds) {
        String[] ids = jobIds.split(StringConstant.COMMA);
        this.jobLogService.deleteJobLogs(ids);
    }

    @GetMapping("excel")
    @PreAuthorize("hasAuthority('job:log:export')")
    public void export(QueryRequest request, JobLog jobLog, HttpServletResponse response) {
        List<JobLog> jobLogs = this.jobLogService.findJobLogs(request, jobLog).getRecords();
        ExcelKit.$Export(JobLog.class, response).downXlsx(jobLogs, false);
    }
}
