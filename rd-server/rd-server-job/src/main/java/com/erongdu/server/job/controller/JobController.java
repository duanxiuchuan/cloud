package com.erongdu.server.job.controller;

import com.erongdu.common.core.entity.CommonResponse;
import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.common.core.entity.constant.StringConstant;
import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.server.job.entity.Job;
import com.erongdu.server.job.service.IJobService;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author erongdu.com
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class JobController {

    private final IJobService jobService;

    @GetMapping
    @PreAuthorize("hasAuthority('job:view')")
    public CommonResponse jobList(QueryRequest request, Job job) {
        Map<String, Object> dataTable = CommonUtil.getDataTable(this.jobService.findJobs(request, job));
        return new CommonResponse().data(dataTable);
    }

    @GetMapping("cron/check")
    public boolean checkCron(String cron) {
        try {
            return CronExpression.isValidExpression(cron);
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('job:add')")
    public void addJob(@Valid Job job) {
        this.jobService.createJob(job);
    }

    @DeleteMapping("{jobIds}")
    @PreAuthorize("hasAuthority('job:delete')")
    public void deleteJob(@NotBlank(message = "{required}") @PathVariable String jobIds) {
        String[] ids = jobIds.split(StringConstant.COMMA);
        this.jobService.deleteJobs(ids);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('job:update')")
    public void updateJob(@Valid Job job) {
        this.jobService.updateJob(job);
    }

    @GetMapping("run/{jobIds}")
    @PreAuthorize("hasAuthority('job:run')")
    public void runJob(@NotBlank(message = "{required}") @PathVariable String jobIds) {
        this.jobService.run(jobIds);
    }

    @GetMapping("pause/{jobIds}")
    @PreAuthorize("hasAuthority('job:pause')")
    public void pauseJob(@NotBlank(message = "{required}") @PathVariable String jobIds) {
        this.jobService.pause(jobIds);
    }

    @GetMapping("resume/{jobIds}")
    @PreAuthorize("hasAuthority('job:resume')")
    public void resumeJob(@NotBlank(message = "{required}") @PathVariable String jobIds) {
        this.jobService.resume(jobIds);
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('job:export')")
    public void export(QueryRequest request, Job job, HttpServletResponse response) {
        List<Job> jobs = this.jobService.findJobs(request, job).getRecords();
        ExcelKit.$Export(Job.class, response).downXlsx(jobs, false);
    }
}
