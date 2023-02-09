package com.cqcnt.exception;

public class ZabbixSessionTimeoutException extends CqcntException{
    static Integer code = 50004;

    public ZabbixSessionTimeoutException(String message) {
        super(code, message);
    }
}
