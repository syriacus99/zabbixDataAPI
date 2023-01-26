package com.cqcnt.util;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class Result<T> {
    private boolean flag;
    private Integer code;
    private String msg;
    private T data;

    public static Result success(Integer code,String msg,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setFlag(true);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result fail(Integer code,String msg,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setFlag(false);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }


    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}