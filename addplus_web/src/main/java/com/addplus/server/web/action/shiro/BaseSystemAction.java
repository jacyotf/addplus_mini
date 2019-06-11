package com.addplus.server.web.action.shiro;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.model.authority.ext.SysLoginUser;
import com.addplus.server.api.model.base.ReturnDataSet;
import com.addplus.server.web.shiro.service.SystemAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 类名：BaseSystemController
 *
 * @author zhangjiehang
 * @version V1.0
 * @date  2017/10/22 19:01
 * @describe 类描述：用于初始登录未授权等方法调用
 */
@RestController
@RequestMapping("/base")
public class BaseSystemAction {

    @Autowired
    private SystemAdminService systemService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void loginUrlAdminPost(@RequestBody SysLoginUser user, @RequestHeader Map<String, String> headerMap) throws Exception {
        systemService.adminLogin(user, headerMap);
    }

    @GetMapping(value = "/logout")
    public void logout() throws Exception {
        systemService.userLoginOut();
    }

    @GetMapping(value = "verify")
    public ReturnDataSet verify() throws Exception {
        ReturnDataSet returnDataSet = new ReturnDataSet();
        returnDataSet.setErrorCode(ErrorCode.SYS_SUCCESS);
        returnDataSet.setDataSet(systemService.getVerify());
        return returnDataSet;
    }

}
