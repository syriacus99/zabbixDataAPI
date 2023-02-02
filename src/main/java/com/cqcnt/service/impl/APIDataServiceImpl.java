package com.cqcnt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cqcnt.config.GlobalVariable;
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
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class APIDataServiceImpl implements com.cqcnt.service.APIDataService {

    @Value("${zabbix.address}")
    private String zabbixAddress;

    @Resource
    private LoginService loginService;

    @Resource
    private GlobalVariable globalVariable;

    @Override
    public String getHostIdByHostName(String name) {
        Object token = globalVariable.getToken();
        String data = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"host.get\",\n" +
                "      \"params\": {\n" +
                "        \"output\" : \"hostid\",\n" +
                "        \"search\":{\n" +
                "            \"name\": \""+ name + "\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"auth\": \""+token.toString()+ "\",\n" +
                "    \"id\": 1\n" +
                "}";
        JSONObject response = JSON.parseObject(
                MyHttpUtil.sendJsonPostRequest(zabbixAddress+"/api_jsonrpc.php", data, String.class)
        );
        Object result =  ((List<JSONObject>)response.get("result")).get(0).get("hostid");
        return result!=null?result.toString():null;
    }

    @Override
    public void getHostGraph(HttpServletRequest request,Integer hostId) {

    }

    @Override
    public void getHostGraphInfo(HttpServletRequest request, Integer hostId) {

    }

    @Override
    public byte[] getGraph(HttpServletRequest request,Integer graphId) throws AuthenticationException, ZabbixConfigException, IOException {
        //List<String> zbxSession = (List<String>) session.getAttribute("zbxSession");
        List<String> zbxSession = (List<String>) globalVariable.getZabbixSession();
        //Object zbxSession = "zbx_session=eyJzZXNzaW9uaWQiOiI3NGJjYWY0ZDYxY2IyOGIzNjM3NjJkNjAyODM5N2VlNCIsInNpZ24iOiI1ODcyMmRkMjdiYTM5ZjY5ZTBiYjdlOTBhMGE1NTkwMWU1ZmM2NzVmNDVkZGExMGE5NzU3ZTdjMDczODA2OTFkIn0%3D; HttpOnly";
        if(zbxSession==null){
            loginService.getZbxSession();
        }

        byte[] picture = MyHttpUtil.getPicture(zabbixAddress+"/chart2.php?graphid=" + graphId,
                zbxSession);
        //+
        //                "&from=now-1h&to=now&height=201&width=1029&profileIdx=web.charts.filter&_=vtxuv2mn"
        FileImageOutputStream imageOutput = new FileImageOutputStream(new File("test.jpg"));//打开输入流
        imageOutput.write(picture, 0, picture.length);//将byte写入硬盘
        imageOutput.close();
        return picture;
    }

    @Override
    public Result getItemsData(HttpServletRequest request, List<Integer> ItemIds) {
        HttpSession session = request.getSession();
        Object token = globalVariable.getToken();
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
                MyHttpUtil.sendJsonPostRequest(zabbixAddress+"/api_jsonrpc.php", data, String.class)
        );
        List<JSONObject> result = (List<JSONObject>) response.get("result");
        return Result.success(200,"获取item成功",result);
    }

    @Override
    public Result getOneItemsData(HttpServletRequest request, Integer itemId) throws GetResultException {
        HttpSession session = request.getSession();
        Object token = globalVariable.getToken();
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

