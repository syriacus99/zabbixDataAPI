package com.cqcnt.service;

import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.GetResultException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.util.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Resource
public interface APIDataService {
    public void getHostGraph(Integer hostId);
    public void getHostGraphInfo(Integer hostId);
    public byte[] getGraph(HttpServletRequest request,Integer graphId) throws AuthenticationException, ZabbixConfigException;
    public Result getItemsData(HttpServletRequest request, List<Integer> ItemId);

    public Result getOneItemsData(HttpServletRequest request, Integer ItemId) throws GetResultException;
}
