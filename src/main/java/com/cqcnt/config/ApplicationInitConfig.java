package com.cqcnt.config;

import com.cqcnt.service.LoginService;
import com.cqcnt.service.ProjectInitService;
import com.cqcnt.util.ProjectUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class ApplicationInitConfig implements ApplicationRunner {

    @Resource
    private ProjectInitService projectInitService;

    @Resource
    private LoginService loginService;

    @Resource
    private GlobalVariable globalVariable;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("获取token");
        loginService.getToken();
        System.out.println("获取token成功");
        System.out.println("获取zabbixsession");
        loginService.getZbxSession();
        System.out.println("获取zabbixsession成功");
        System.out.println("读取zabbixHostConfig");
        projectInitService.loadZabbixHostConfig();
        System.out.println("读取成功");
        System.out.println(globalVariable.getHostListMap());
    }
}
