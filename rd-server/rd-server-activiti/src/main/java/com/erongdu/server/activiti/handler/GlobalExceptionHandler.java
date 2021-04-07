package com.erongdu.server.activiti.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.erongdu.common.core.handler.BaseExceptionHandler;

/**
 * 全局异常处理
 * @author erongdu.com
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends BaseExceptionHandler {
}
