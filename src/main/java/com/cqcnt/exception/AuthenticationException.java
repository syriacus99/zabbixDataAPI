package com.cqcnt.exception;

public class AuthenticationException extends CqcntException{

    static Integer code = 50003;

    public AuthenticationException(String message) {
        super(code, message);
    }
}
