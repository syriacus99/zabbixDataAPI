package com.cqcnt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.GetResultException;
import com.cqcnt.exception.LoginException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.service.LoginService;
import com.cqcnt.util.MyHttpUtil;
import com.cqcnt.util.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class APIDataServiceImpl implements com.cqcnt.service.APIDataService {

    @Value("${zabbix.address}")
    private String zabbixAddress;

    @Resource
    private LoginService loginService;

    @Override
    public void getHostGraph(Integer hostId) {

    }

    @Override
    public void getHostGraphInfo(Integer hostId) {

    }

    @Override
    public byte[] getGraph(HttpServletRequest request,Integer graphId) throws AuthenticationException, ZabbixConfigException {
        HttpSession session = request.getSession();
        Object zbxSession = session.getAttribute("zbxSession");
        if(zbxSession==null){
            loginService.getZbxSession(request);
        }

        byte[] picture = MyHttpUtil.getPicture("http://117.59.224.111/chart2.php?graphid=" + graphId +
                "&from=now-1h&to=now&height=201&width=1029&profileIdx=web.charts.filter&_=vtxuv2mn",
                zbxSession);

        return picture;
    }

    @Override
    public Result getItemsData(HttpServletRequest request, List<Integer> ItemIds) {
        HttpSession session = request.getSession();
        Object token = session.getAttribute("token");
        String data = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"item.get\",\n" +
                "    \"params\": {\n" +
                "        \"output\": [\"name\",\"lastvalue\",\"prevvalue\"],\n" +
                "        \"itemids\":" + ItemIds.toString() +"\",\n" +
                "        \"sortfield\": \"name\"\n" +
                "    },\n" +
                "    \"auth\": \""+token.toString()+ "\"\n" +
                "    \"id\": 1\n" +
                "}";
        JSONObject response = JSON.parseObject(
                MyHttpUtil.sendJsonPostRequest(zabbixAddress, data, String.class)
        );
        List<JSONObject> result = (List<JSONObject>) response.get("result");
        return Result.success(200,"获取item成功",result);
    }

    @Override
    public Result getOneItemsData(HttpServletRequest request, Integer itemId) throws GetResultException {
        HttpSession session = request.getSession();
        Object token = session.getAttribute("token");
        String data = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"item.get\",\n" +
                "    \"params\": {\n" +
                "        \"output\": [\"name\",\"lastvalue\",\"prevvalue\"],\n" +
                "        \"itemids\": [" + itemId +"],\n" +
                "        \"sortfield\": \"name\"\n" +
                "    },\n" +
                "    \"auth\": \""+token.toString()+ "\",\n" +
                "    \"id\": 1\n" +
                "}";
        JSONObject response = JSON.parseObject(
                MyHttpUtil.sendJsonPostRequest(zabbixAddress, data, String.class)
        );
        System.out.println(data);
        System.out.println(response);
        List<JSONObject> result = (List<JSONObject>) response.get("result");

        if(result==null){
            throw new GetResultException("获取监控项 "+ itemId+" 数据失败");
        }
        return Result.success(200,"获取item成功",result.get(0));
    }
}

