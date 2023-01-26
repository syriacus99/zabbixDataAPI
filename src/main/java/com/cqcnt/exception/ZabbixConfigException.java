package com.cqcnt.exception;

public class ZabbixConfigException extends CqcntException{

    public static Integer code = 500002;

    public ZabbixConfigException(String message) {
        super(code, message);
    }
}
