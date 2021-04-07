package com.erongdu.server.system.controller;

import com.erongdu.common.core.entity.CommonResponse;
import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.common.core.entity.constant.StringConstant;
import com.erongdu.common.core.entity.system.LoginLog;
import com.erongdu.common.core.entity.system.SystemUser;
import com.erongdu.common.core.exception.CommonException;
import com.erongdu.common.core.utils.CommonUtil;
import com.erongdu.server.system.annotation.ControllerEndpoint;
import com.erongdu.server.system.service.ILoginLogService;
import com.erongdu.server.system.service.IUserDataPermissionService;
import com.erongdu.server.system.service.IUserService;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author erongdu.com
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final IUserService userService;
    private final IUserDataPermissionService userDataPermissionService;
    private final ILoginLogService loginLogService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("success")
    public void loginSuccess(HttpServletRequest request) {
        String currentUsername = CommonUtil.getCurrentUsername();
        // update last login time
        this.userService.updateLoginTime(currentUsername);
        // save login log
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(currentUsername);
        loginLog.setSystemBrowserInfo(request.getHeader("user-agent"));
        this.loginLogService.saveLoginLog(loginLog);
    }

    @GetMapping("index")
    public CommonResponse index() {
        Map<String, Object> data = new HashMap<>(5);
        // 获取系统访问记录
        Long totalVisitCount = loginLogService.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = loginLogService.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount);
        Long todayIp = loginLogService.findTodayIp();
        data.put("todayIp", todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastTenVisitCount = loginLogService.findLastTenDaysVisitCount(null);
        data.put("lastTenVisitCount", lastTenVisitCount);
        SystemUser param = new SystemUser();
        param.setUsername(CommonUtil.getCurrentUsername());
        List<Map<String, Object>> lastTenUserVisitCount = loginLogService.findLastTenDaysVisitCount(param);
        data.put("lastTenUserVisitCount", lastTenUserVisitCount);
        return new CommonResponse().data(data);
    }


    @GetMapping
    @PreAuthorize("hasAuthority('user:view')")
    public CommonResponse userList(QueryRequest queryRequest, SystemUser user) {
        Map<String, Object> dataTable = CommonUtil.getDataTable(userService.findUserDetailList(user, queryRequest));
        return new CommonResponse().data(dataTable);
    }

    @GetMapping("check/{username}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username) == null;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:add')")
    @ControllerEndpoint(operation = "新增用户", exceptionMessage = "新增用户失败")
    public void addUser(@Valid SystemUser user) {
        this.userService.createUser(user);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('user:update')")
    @ControllerEndpoint(operation = "修改用户", exceptionMessage = "修改用户失败")
    public void updateUser(@Valid SystemUser user) {
        this.userService.updateUser(user);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('user:update')")
    public CommonResponse findUserDataPermissions(@NotBlank(message = "{required}") @PathVariable String userId) {
        String dataPermissions = this.userDataPermissionService.findByUserId(userId);
        return new CommonResponse().data(dataPermissions);
    }

    @DeleteMapping("/{userIds}")
    @PreAuthorize("hasAuthority('user:delete')")
    @ControllerEndpoint(operation = "删除用户", exceptionMessage = "删除用户失败")
    public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) {
        String[] ids = userIds.split(StringConstant.COMMA);
        this.userService.deleteUsers(ids);
    }

    @PutMapping("profile")
    @ControllerEndpoint(exceptionMessage = "修改个人信息失败")
    public void updateProfile(@Valid SystemUser user) throws CommonException {
        this.userService.updateProfile(user);
    }

    @PutMapping("avatar")
    @ControllerEndpoint(exceptionMessage = "修改头像失败")
    public void updateAvatar(@NotBlank(message = "{required}") String avatar) {
        this.userService.updateAvatar(avatar);
    }

    @GetMapping("password/check")
    public boolean checkPassword(@NotBlank(message = "{required}") String password) {
        String currentUsername = CommonUtil.getCurrentUsername();
        SystemUser user = userService.findByName(currentUsername);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    @PutMapping("password")
    @ControllerEndpoint(exceptionMessage = "修改密码失败")
    public void updatePassword(@NotBlank(message = "{required}") String password) {
        userService.updatePassword(password);
    }

    @PutMapping("password/reset")
    @PreAuthorize("hasAuthority('user:reset')")
    @ControllerEndpoint(exceptionMessage = "重置用户密码失败")
    public void resetPassword(@NotBlank(message = "{required}") String usernames) {
        String[] usernameArr = usernames.split(StringConstant.COMMA);
        this.userService.resetPassword(usernameArr);
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('user:export')")
    @ControllerEndpoint(operation = "导出用户数据", exceptionMessage = "导出Excel失败")
    public void export(QueryRequest queryRequest, SystemUser user, HttpServletResponse response) {
        List<SystemUser> users = this.userService.findUserDetailList(user, queryRequest).getRecords();
        ExcelKit.$Export(SystemUser.class, response).downXlsx(users, false);
    }
}
