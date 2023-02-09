package com.cqcnt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cqcnt.config.GlobalVariable;
import com.cqcnt.dao.DataVCityData;
import com.cqcnt.dao.Geometry;
import com.cqcnt.dao.HostInfoDao;
import com.cqcnt.dao.ItemInfoDao;
import com.cqcnt.dao.dto.CityDataDto;
import com.cqcnt.dao.form.GetGraphIdByItemIdForm;
import com.cqcnt.dao.form.GetItemsInfoByHostIdForm;
import com.cqcnt.exception.*;
import com.cqcnt.service.LoginService;
import com.cqcnt.util.MyHttpUtil;
import com.cqcnt.util.ProjectUtil;
import com.cqcnt.util.Result;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class APIDataServiceImpl implements com.cqcnt.service.APIDataService {

    @Value("${zabbix.address}")
    private String zabbixAddress;

    @Resource
    private LoginService loginService;

    @Resource
    private GlobalVariable globalVariable;

    @Resource
    private ProjectUtil projectUtil;

    @Value("${zabbix.interfacesToGeometry}")
    private String interfacesToGeometry;

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

    @Async
    @Override
    public List<GetItemsInfoByHostIdForm> getItemsInfoByHostId(Integer hostId) {
        Map<String, HostInfoDao> hostListMap = globalVariable.getHostListMapByHostId();
        HostInfoDao hostInfoDao = hostListMap.get(hostId.toString());
        List<String> interfaces = hostInfoDao.getInterfaces();
        String tags = "{\n" +
                "                \"tag\": \"description\",\n" +
                "                \"value\": \""+ interfaces.get(0) +"\",\n" +
                "                \"operator\": 0\n" +
                "            }\n";
        if (interfaces.size() > 1){
            for(int i=1; i<interfaces.size();i++){
                tags += ",";
                tags += "            {\n" +
                        "                \"tag\": \"description\",\n" +
                        "                \"value\": \""+ interfaces.get(i) +"\",\n" +
                        "                \"operator\": 0\n" +
                        "            }\n";
            }
        }
        Object token = globalVariable.getToken();
        String data = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"item.get\",\n" +
                "    \"params\": {\n" +
                "        \"output\": [\"itemid\",\"name\",\"lastvalue\",\"prevvalue\"],\n" +
                "        \"hostid\": "+hostId+ ",\n" +
                "        \"tags\":[\n" +
                tags+
//                "            {\n" +
//                "                \"tag\": \"description\",\n" +
//                "                \"value\": \"To_\",\n" +
//                "                \"operator\": 0\n" +
//                "            }\n" +


                "        ],\n" +
                "        \"search\":{\n" +
                "            \"name\": \"Bits sent\"\n" +
                "        },\n" +
                "        \"sortfield\": \"name\"\n" +
                "    },\n" +
                "    \"auth\": \""+token+"\",\n" +
                "    \"id\": 1\n" +
                "}";
        JSONObject response = JSON.parseObject(
                MyHttpUtil.sendJsonPostRequest(zabbixAddress+"/api_jsonrpc.php", data, String.class)
        );
        JSONArray jsonArray =  (JSONArray)response.get("result");
        List<GetItemsInfoByHostIdForm> result = jsonArray.toJavaList(GetItemsInfoByHostIdForm.class);
        return result;
    }

    @Override
    public List<HostInfoDao> getHostByCity(String cityId) throws CqcntException {
        Map<String, Set<String>> hostListMapByCity = globalVariable.getHostListMapByCity();
        Map<String, HostInfoDao> hostListMapByHostId = globalVariable.getHostListMapByHostId();
        List<HostInfoDao> hostInfoDaoList = new ArrayList<>();
        Set<String> hostIds = hostListMapByCity.get(cityId);
        if(hostIds==null){
            return hostInfoDaoList;
        }
        for(String eachHostId : hostIds){
            HostInfoDao hostInfoDao = hostListMapByHostId.get(eachHostId);
            if(hostInfoDao==null){
                throw new CqcntException(99999,"getHostByCity service error");
            }
            hostInfoDaoList.add(hostInfoDao);
        }
        return hostInfoDaoList;
    }

    @Override
    public String getGraphIdByItemId(String itemId) {
        Object token = globalVariable.getToken();
        String data = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"graphitem.get\",\n" +
                "    \"params\": {\n" +
                "        \"output\": [\"graphid\"],\n" +
                "        \"itemids\": \""+itemId+"\"\n" +
                "    },\n" +
                "    \"auth\": \""+token+"\",\n" +
                "    \"id\": 1\n" +
                "}";
        JSONObject response = JSON.parseObject(
                MyHttpUtil.sendJsonPostRequest(zabbixAddress+"/api_jsonrpc.php", data, String.class)
        );
        JSONArray jsonArray =  (JSONArray)response.get("result");
        List<GetGraphIdByItemIdForm> result = jsonArray.toJavaList(GetGraphIdByItemIdForm.class);
        return result.get(0).getGraphid();
    }

    @Override
    public Result getOneItemsData(Integer itemId) throws GetResultException {
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

    @Override
    public CityDataDto getOneCityItemsData(String cityCode) {
        Map<String, Set<String>> hostListMapByCity = globalVariable.getHostListMapByCity();
        Map<String, HostInfoDao> hostListMapByHostId = globalVariable.getHostListMapByHostId();
        Map<String, List<Double>> interfacesToGeometry = globalVariable.getInterfacesToGeometry();
        CityDataDto cityDataDto = new CityDataDto();
        List<DataVCityData> dataVCityDataList = new ArrayList<>();
        Set<String> hosts = hostListMapByCity.get(cityCode);
        int i = 0;
        for(String eachHost : hosts){
            HostInfoDao hostInfoDao = hostListMapByHostId.get(eachHost);
            Map<String, Long[]> stringMap = projectUtil.itemSum(hostInfoDao);
            //对于每一个监控端口的飞线图数据
            for(String eachInterface : stringMap.keySet()){
                DataVCityData dataVCityData = new DataVCityData();
                dataVCityData.setId(i);
                dataVCityData.setValue(stringMap.get(eachInterface)[0]+stringMap.get(eachInterface)[1]);
                List<Double> from = hostInfoDao.getGeometry();
                List<Double> to = interfacesToGeometry.get(eachInterface);
                dataVCityData.setGeometry(new Geometry(from,to));
                dataVCityDataList.add(dataVCityData);
                i++;
            }
        }
        cityDataDto.setDataVCityData(dataVCityDataList);
        return cityDataDto;
    }
}

