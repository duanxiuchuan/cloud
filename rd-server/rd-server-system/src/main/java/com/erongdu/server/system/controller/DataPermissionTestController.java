package com.erongdu.server.system.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.entity.CommonResponse;
import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.common.core.entity.system.DataPermissionTest;
import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.server.system.service.IDataPermissionTestService;

import java.util.Map;

/**
 * Controller
 *
 * @author erongdu.com
 * @date 2020-04-14 15:25:33
 */
@Slf4j
@RestController
@RequestMapping("dataPermissionTest")
@RequiredArgsConstructor
public class DataPermissionTestController {

    private final IDataPermissionTestService dataPermissionTestService;

    @GetMapping("list")
    @PreAuthorize("hasAuthority('others:datapermission')")
    public CommonResponse dataPermissionTestList(QueryRequest request, DataPermissionTest dataPermissionTest) {
        Map<String, Object> dataTable = CommonUtil.getDataTable(this.dataPermissionTestService.findDataPermissionTests(request, dataPermissionTest));
        return new CommonResponse().data(dataTable);
    }
}
