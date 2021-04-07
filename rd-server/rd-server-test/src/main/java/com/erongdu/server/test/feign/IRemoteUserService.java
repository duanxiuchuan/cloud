package com.erongdu.server.test.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.erongdu.common.core.entity.CommonResponse;
import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.common.core.entity.constant.RdServerConstant;
import com.erongdu.common.core.entity.system.SystemUser;
import com.erongdu.server.test.feign.fallback.RemoteUserServiceFallback;

/**
 * @author erongdu.com
 */
@FeignClient(value = RdServerConstant.RD_SERVER_SYSTEM, contextId = "userServiceClient", fallbackFactory = RemoteUserServiceFallback.class)
public interface IRemoteUserService {

    /**
     * remote /user endpoint
     *
     * @param queryRequest queryRequest
     * @param user         user
     * @return FebsResponse
     */
    @GetMapping("user")
    CommonResponse userList(@RequestParam("queryRequest") QueryRequest queryRequest, @RequestParam("user") SystemUser user);
}
