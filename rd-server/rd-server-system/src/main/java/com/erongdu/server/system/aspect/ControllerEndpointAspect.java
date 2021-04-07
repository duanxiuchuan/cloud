package com.erongdu.server.system.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.erongdu.common.core.exception.CommonException;
import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.server.system.annotation.ControllerEndpoint;
import com.erongdu.server.system.service.ILogService;

import java.lang.reflect.Method;

/**
 * @author erongdu.com
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class ControllerEndpointAspect extends BaseAspectSupport {

    private final ILogService logService;

    @Pointcut("execution(* com.erongdu.server.system.controller.*.*(..)) && @annotation(com.erongdu.server.system.annotation.ControllerEndpoint)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws CommonException {
        Object result;
        Method targetMethod = resolveMethod(point);
        ControllerEndpoint annotation = targetMethod.getAnnotation(ControllerEndpoint.class);
        String operation = annotation.operation();
        long start = System.currentTimeMillis();
        try {
            result = point.proceed();
            if (StringUtils.isNotBlank(operation)) {
                String username = CommonUtil.getCurrentUsername();
                String ip = CommonUtil.getHttpServletRequestIpAddress();
                logService.saveLog(point, targetMethod, ip, operation, username, start);
            }
            return result;
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            String exceptionMessage = annotation.exceptionMessage();
            String message = throwable.getMessage();
            String error = CommonUtil.containChinese(message) ? exceptionMessage + "，" + message : exceptionMessage;
            throw new CommonException(error);
        }
    }
}



