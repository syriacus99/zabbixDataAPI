package com.cqcnt.exception;

public class TokenTimeoutException extends CqcntException{
    static Integer code = 50005;

    public TokenTimeoutException(String message) {
        super(code, message);
    }
}