package com.cqcnt.exception;

public class LoginException extends CqcntException {

    static public Integer code = 50000;
    public LoginException(String message) {
        super(code, message);
    }
}
