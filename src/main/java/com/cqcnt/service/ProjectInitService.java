package com.cqcnt.service;

import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.exception.ZabbixSessionTimeoutException;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Resource
public interface ProjectInitService {

    public void loadZabbixHostConfig();

    public void getItemsInfo() throws ExecutionException, InterruptedException;

    public void getAllItemGraph() throws AuthenticationException, ZabbixConfigException, IOException, ZabbixSessionTimeoutException;

    public void getInterfacesToGeometry();
}
