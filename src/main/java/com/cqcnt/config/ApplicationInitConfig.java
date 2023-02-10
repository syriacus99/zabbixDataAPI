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
        System.out.println("成功");
        System.out.println("获取zabbixsession");
        loginService.getZbxSession();
        System.out.println("成功");
        System.out.println("读取zabbixHostConfig");
        projectInitService.loadZabbixHostConfig();
        System.out.println("成功");
        System.out.println(globalVariable.getHostListMapByHostId());
        System.out.println(globalVariable.getHostListMapByCity());
        projectInitService.getInterfacesToGeometry();
        System.out.println(globalVariable.getInterfacesToGeometry());
        System.out.println("读取interfaces信息");
        projectInitService.getItemsInfo();
        System.out.println("成功");
        System.out.println(globalVariable.getHostListMapByHostId());
        System.out.println("预缓存items图片");
        //projectInitService.getAllItemGraph();
        System.out.println("成功");
    }
}
