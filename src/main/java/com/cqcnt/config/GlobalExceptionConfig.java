package com.cqcnt.config;

import com.cqcnt.exception.CqcntException;
import com.cqcnt.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionConfig {

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
