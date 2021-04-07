package com.erongdu.auth.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.erongdu.common.core.entity.CommonResponse;
import com.erongdu.common.core.utils.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author erongdu.com
 */
@Component
public class WebLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exception) throws IOException {
        String message;
        if (exception instanceof BadCredentialsException) {
            message = "用户名或密码错误！";
        } else if (exception instanceof LockedException) {
            message = "用户已被锁定！";
        } else {
            message = "认证失败，请联系网站管理员！";
        }
        CommonResponse febsResponse = new CommonResponse().message(message);
        CommonUtil.makeFailureResponse(httpServletResponse, febsResponse);
    }
}
