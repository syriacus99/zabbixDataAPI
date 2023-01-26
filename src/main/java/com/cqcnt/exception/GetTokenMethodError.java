package com.cqcnt.exception;

public class GetTokenMethodError extends CqcntException{

    static Integer code = 50001;

    public GetTokenMethodError(String message) {
        super(code, message);
    }
}
