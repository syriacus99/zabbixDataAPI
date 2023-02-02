package com.cqcnt.service;

import javax.annotation.Resource;

@Resource
public interface ProjectInitService {

    public void loadZabbixHostConfig();

    public void getItemsInfo();
}
