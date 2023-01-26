package com.cqcnt.controller;

import com.alibaba.fastjson.JSONObject;
import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.GetTokenMethodError;
import com.cqcnt.exception.LoginException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.service.LoginService;
import com.cqcnt.util.MyHttpUtil;
import com.cqcnt.util.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@RestController
@RequestMapping
public class LoginController {

    @Resource
    private LoginService loginService;

    @RequestMapping ("/login")
    public Result login(HttpServletRequest request) throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, LoginException {
        try {
            loginService.getToken(request);
        } catch (Exception e) {
            throw new LoginException("获取登录token失败");
        }
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("token"));
        return Result.success(200,"1",null);
    }

    @RequestMapping ("/test")
    public Result test(HttpServletRequest request) throws IntrospectionException, ZabbixConfigException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, GetTokenMethodError, AuthenticationException {
//        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
//        map.add("username","Admin");
//        map.add("password","Cqcnt@123.,");
//        map.add("enter","Sign in");
//        MyHttpUtil.sendFormPostRequest("http://117.59.224.111/index.php",
//                map);
        loginService.getAllHost(request);
        return Result.success(200,"1",null);
    }
}
