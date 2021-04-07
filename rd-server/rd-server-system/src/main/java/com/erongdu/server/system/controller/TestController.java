package com.erongdu.server.system.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.entity.system.TradeLog;
import com.erongdu.server.system.service.ITradeLogService;

/**
 * @author erongdu.com
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final ITradeLogService tradeLogService;

    @PostMapping("package/send")
    public void packageSend(@RequestBody TradeLog tradeLog) {
        this.tradeLogService.packageAndSend(tradeLog);
    }
}
