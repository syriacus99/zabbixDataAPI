package com.cqcnt.exception;

public class GetResultException extends CqcntException{

    static public Integer code = 50004;

    public GetResultException(String message) {
        super(code, message);
    }
}
