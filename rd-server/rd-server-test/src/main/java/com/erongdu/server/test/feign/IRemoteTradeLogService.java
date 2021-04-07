package com.erongdu.server.test.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.erongdu.common.core.entity.constant.RdServerConstant;
import com.erongdu.common.core.entity.system.TradeLog;
import com.erongdu.server.test.feign.fallback.RemoteTradeLogServiceFallback;

/**
 * @author erongdu.com
 */
@FeignClient(value = RdServerConstant.RD_SERVER_SYSTEM, contextId = "tradeLogServiceClient", fallbackFactory = RemoteTradeLogServiceFallback.class)
public interface IRemoteTradeLogService {

    /**
     * 打包派送
     *
     * @param tradeLog 交易日志
     */
    @PostMapping("package/send")
    void packageAndSend(@RequestBody TradeLog tradeLog);
}
