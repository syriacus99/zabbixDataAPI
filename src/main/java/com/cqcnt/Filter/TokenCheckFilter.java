package com.cqcnt.Filter;


import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.service.LoginService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Component
public class TokenCheckFilter implements Filter {

    @Resource
    private LoginService loginService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpSession session =  ((HttpServletRequest) servletRequest).getSession();
        Object token = session.getAttribute("token");
        if (token==null){
            try {
                loginService.getToken((HttpServletRequest) servletRequest);
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (ZabbixConfigException e) {
                throw new RuntimeException(e);
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }
        }

        Object zabbix = session.getAttribute("zbxSession");
        if(zabbix==null){
            try {
                loginService.getZbxSession((HttpServletRequest) servletRequest);
            } catch (ZabbixConfigException e) {
                throw new RuntimeException(e);
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }


}
