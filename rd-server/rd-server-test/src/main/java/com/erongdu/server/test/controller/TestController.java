package com.erongdu.server.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.entity.CommonResponse;
import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.common.core.entity.system.SystemUser;
import com.erongdu.common.core.entity.system.TradeLog;
import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.server.test.feign.IRemoteUserService;
import com.erongdu.server.test.service.ITradeLogService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author erongdu.com
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class TestController {

    private final IRemoteUserService remoteUserService;
    private final ITradeLogService tradeLogService;

    /**
     * 用于演示 Feign调用受保护的远程方法
     */
    @GetMapping("user/list")
    public CommonResponse getRemoteUserList(QueryRequest request, SystemUser user) {
        return remoteUserService.userList(request, user);
    }

    /**
     * 测试分布式事务
     */
    @GetMapping("pay")
    public void orderAndPay(TradeLog tradeLog) {
        this.tradeLogService.orderAndPay(tradeLog);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("user")
    public Map<String, Object> currentUser() {
        Map<String, Object> map = new HashMap<>(5);
        map.put("currentUser", CommonUtil.getCurrentUser());
        map.put("currentUsername", CommonUtil.getCurrentUsername());
        map.put("currentUserAuthority", CommonUtil.getCurrentUserAuthority());
        map.put("currentTokenValue", CommonUtil.getCurrentTokenValue());
        map.put("currentRequestIpAddress", CommonUtil.getHttpServletRequestIpAddress());
        return map;
    }
}
