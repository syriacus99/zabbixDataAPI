package com.cqcnt.service;

import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.GetTokenMethodError;
import com.cqcnt.exception.ZabbixConfigException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@Resource
public interface LoginService {
    public void getToken(HttpServletRequest request) throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, ZabbixConfigException, AuthenticationException;

    public void getAllHost(HttpServletRequest request) throws IntrospectionException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, GetTokenMethodError, ZabbixConfigException, AuthenticationException;

    public void getZbxSession(HttpServletRequest request) throws ZabbixConfigException, AuthenticationException;
}
