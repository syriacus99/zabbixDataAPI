package com.cqcnt.config;

import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.exception.ZabbixSessionTimeoutException;
import com.cqcnt.service.LoginService;
import com.cqcnt.service.ProjectInitService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

@Component
@EnableScheduling
public class TimedTaskConfig {

    @Resource
    private LoginService loginService;

    @Resource
    private ProjectInitService projectInitService;

    @Scheduled(cron ="* * */6 * * ?")
    private void reGetToken() throws AuthenticationException, IntrospectionException, ZabbixConfigException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        loginService.getToken();
    }

    @Scheduled(cron ="* * */1 * * ?")
    private void reGetZabbixSession() throws AuthenticationException, ZabbixConfigException {
        loginService.getZbxSession();
    }

    @Scheduled(cron ="* */30 * * * ?")
    private void reGetItemInfo() throws ExecutionException, InterruptedException, AuthenticationException, ZabbixConfigException, IOException, ZabbixSessionTimeoutException {
        projectInitService.getItemsInfo();
        projectInitService.getAllItemGraph();
    }

}
