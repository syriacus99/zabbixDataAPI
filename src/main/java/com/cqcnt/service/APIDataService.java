package com.cqcnt.service;

import com.cqcnt.dao.HostInfoDao;
import com.cqcnt.dao.dto.CityDataDto;
import com.cqcnt.dao.form.GetItemsInfoByHostIdForm;
import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.CqcntException;
import com.cqcnt.exception.GetResultException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.util.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Resource
public interface APIDataService {

    //根据主机名字查询其id（主机名字可是zabbix全名的部分）
    public String getHostIdByHostName(String name);
    public void getHostGraph(HttpServletRequest request,Integer hostId);
    public void getHostGraphInfo(HttpServletRequest request,Integer hostId);
    public byte[] getGraph(HttpServletRequest request,Integer graphId) throws AuthenticationException, ZabbixConfigException, IOException;
    public Result getItemsData(HttpServletRequest request, List<Integer> ItemId);
    //根据hostid得到其所有配置文件里设置的监控项数据（itemid,name.prevvalue,lastvalue)
    public CompletableFuture<List<GetItemsInfoByHostIdForm>> getItemsInfoByHostId(Integer hostId);
    //根据城市id获取host
    public List<HostInfoDao> getHostByCity(String cityId) throws CqcntException;
    //根据item获取graphId
    public String getGraphIdByItemId(String itemId);
    public Result getOneItemsData(Integer ItemId) throws GetResultException;

    //根据cityId获取里面所有item数据
    public CompletableFuture<CityDataDto> getCityItemsData();
}
