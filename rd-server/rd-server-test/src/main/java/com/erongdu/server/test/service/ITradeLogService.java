package com.erongdu.server.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erongdu.common.core.entity.system.TradeLog;

/**
 * @author erongdu.com
 */
public interface ITradeLogService extends IService<TradeLog> {

    /**
     * 下单并支付
     *
     * @param tradeLog 交易日志
     */
    void orderAndPay(TradeLog tradeLog);
}