package com.erongdu.server.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erongdu.common.core.entity.system.TradeLog;

/**
 * @author erongdu.com
 */
public interface ITradeLogService extends IService<TradeLog> {

    /**
     * 打包并派送
     *
     * @param tradeLog 交易日志
     */
    void packageAndSend(TradeLog tradeLog);
}
