package com.cqcnt.config;

import com.cqcnt.exception.*;
import com.cqcnt.service.LoginService;
import com.cqcnt.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

import static javafx.application.Platform.exit;

@RestControllerAdvice
public class GlobalExceptionConfig {

    @Resource
    private LoginService loginService;

    @ExceptionHandler(AuthenticationException.class)
    public void handleWoniuxyException(AuthenticationException e){
        System.out.println("用户名密码错误,启动失败");
        exit();
    }

    @ExceptionHandler(TokenTimeoutException.class)
    public void handleTokenTimeoutException(TokenTimeoutException e) throws AuthenticationException, IntrospectionException, ZabbixConfigException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        loginService.getToken();
    }

    @ExceptionHandler(ZabbixSessionTimeoutException.class)
    public void ZabbixSessionTimeoutException(ZabbixSessionTimeoutException e) throws AuthenticationException, ZabbixConfigException {
        loginService.getZbxSession();
    }

    @ExceptionHandler(ZabbixConfigException.class)
    public void handleZabbixConfigException(ZabbixConfigException e){
        System.out.println("Zabbix配置错误,启动失败");
        exit();
    }

    @ExceptionHandler(CqcntException.class)
    public Result handleCqcntException(CqcntException e){
        return Result.fail(e.getCode(),e.getMessage(),null);
    }


    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        System.out.println(e);
        return Result.fail(500,"服务器繁忙",null);
    }
}
