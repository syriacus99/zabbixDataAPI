package com.cqcnt.exception;

import lombok.Data;

@Data
public class CqcntException extends Exception{
    private Integer code;
    public CqcntException(Integer code,String message){
        super(message);
        this.code = code;
    }
}
