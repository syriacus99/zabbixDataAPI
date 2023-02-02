package com.cqcnt.config;

import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.CqcntException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static javafx.application.Platform.exit;

@RestControllerAdvice
public class GlobalExceptionConfig {

    @ExceptionHandler(AuthenticationException.class)
    public void handleWoniuxyException(AuthenticationException e){
        System.out.println("用户名密码错误,启动失败");
        exit();
    }

    @ExceptionHandler(ZabbixConfigException.class)
    public void handleWoniuxyException(ZabbixConfigException e){
        System.out.println("Zabbix配置错误,启动失败");
        exit();
    }

    @ExceptionHandler(CqcntException.class)
    public Result handleWoniuxyException(CqcntException e){
        return Result.fail(e.getCode(),e.getMessage(),null);
    }


    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        System.out.println(e);
        return Result.fail(500,"服务器繁忙",null);
    }
}
