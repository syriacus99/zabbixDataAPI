package com.cqcnt.service;

import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.GetResultException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.util.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Resource
public interface APIDataService {

    //根据主机名字查询其id（主机名字可是zabbix全名的部分）
    public String getHostIdByHostName(String name);
    public void getHostGraph(HttpServletRequest request,Integer hostId);
    public void getHostGraphInfo(HttpServletRequest request,Integer hostId);
    public byte[] getGraph(HttpServletRequest request,Integer graphId) throws AuthenticationException, ZabbixConfigException, IOException;
    public Result getItemsData(HttpServletRequest request, List<Integer> ItemId);

    public Result getOneItemsData(HttpServletRequest request, Integer ItemId) throws GetResultException;
}
