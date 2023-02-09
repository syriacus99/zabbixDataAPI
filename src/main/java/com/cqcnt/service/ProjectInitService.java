package com.cqcnt.service;

import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.exception.ZabbixSessionTimeoutException;

import javax.annotation.Resource;
import java.io.IOException;

@Resource
public interface ProjectInitService {

    public void loadZabbixHostConfig();

    public void getItemsInfo();

    public void getAllItemGraph() throws AuthenticationException, ZabbixConfigException, IOException, ZabbixSessionTimeoutException;

    public void getInterfacesToGeometry();
}
