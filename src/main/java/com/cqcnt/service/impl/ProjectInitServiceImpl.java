package com.cqcnt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cqcnt.config.GlobalVariable;
import com.cqcnt.dao.HostInfoDao;
import com.cqcnt.dao.ItemInfoDao;
import com.cqcnt.dao.form.GetItemsInfoByHostIdForm;
import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.exception.ZabbixSessionTimeoutException;
import com.cqcnt.service.APIDataService;
import com.cqcnt.service.LoginService;
import com.cqcnt.service.ProjectInitService;
import com.cqcnt.util.BeanCopyUtil;
import com.cqcnt.util.MyHttpUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.stream.FileImageOutputStream;
import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ProjectInitServiceImpl implements ProjectInitService {

    @Value("${zabbix.config}")
    private String zabbixHostConfigPath;

    @Value("${zabbix.interfacesToGeometry}")
    private String interfacesToGeometryPath;

    @Value("${zabbix.address}")
    private String zabbixAddress;

    @Resource
    private GlobalVariable globalVariable;

    @Resource
    private APIDataService apiDataService;

    @Resource
    private LoginService loginService;

    @Override
    public void loadZabbixHostConfig(){
        String rootPath = System.getProperty("user.dir");
        //1.读取JSON文件
        String jsonStr = null;
        try {
            File file = new File(rootPath,zabbixHostConfigPath).getAbsoluteFile();
            Reader reader = new InputStreamReader(new FileInputStream(file), "Utf-8");
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            jsonStr = sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JSONArray result = JSONObject.parseArray(jsonStr);
        List<HostInfoDao> hostInfoDaos = result.toJavaList(HostInfoDao.class);
        Map<String, HostInfoDao> hostListMap = globalVariable.getHostListMapByHostId();
        Map<String, Set<String>> hostListMapByCity = globalVariable.getHostListMapByCity();
        System.out.println(globalVariable.getToken());
        //获取主机名对应的hostid 如果没获取到，则忽略
        for (HostInfoDao eachHost : hostInfoDaos){
            String hostId = apiDataService.getHostIdByHostName(eachHost.getName());
            String city = eachHost.getCity();
            if (hostId != null){
                eachHost.setHostid(Integer.valueOf(hostId));
                hostListMap.put(hostId,eachHost);
                //如果存在则获取city的值，并操作值的set添加数据hostId。
                //如果不存在，则调用方法，新创建set结构，将hostId添加到set中，再存入到hashMap中。
                hostListMapByCity.computeIfAbsent(city, k -> new HashSet<>()).add(hostId);
            }
        }
    }

    @Override
    public void getItemsInfo() throws ExecutionException, InterruptedException {
        Object token = globalVariable.getToken();
        Map<String, HostInfoDao> hostListMapByHostId = globalVariable.getHostListMapByHostId();
        for(String eachHost : hostListMapByHostId.keySet()){
            //List<GetItemsInfoByHostIdForm> itemsInfos = apiDataService.getItemsInfoByHostId(Integer.valueOf(eachHost));
            CompletableFuture<List<GetItemsInfoByHostIdForm>> itemsInfoByHostId = apiDataService.getItemsInfoByHostId(Integer.valueOf(eachHost));
            List<ItemInfoDao> itemInfoDaos = BeanCopyUtil.copyList(itemsInfoByHostId.get(), ItemInfoDao.class);
            for(ItemInfoDao eachItem : itemInfoDaos){
                String itemid = eachItem.getItemid();
                String graphId = apiDataService.getGraphIdByItemId(itemid);
                eachItem.setGraphid(graphId);
            }
            hostListMapByHostId.get(eachHost).setItemInfoDao(itemInfoDaos);
        }
    }

    @Override
    @Async
    public void getAllItemGraph() throws AuthenticationException, ZabbixConfigException, IOException, ZabbixSessionTimeoutException {
        //获得全局变量中的zabbixSession以及初始化后的hostListMapByHostId
        Object zabbixSession = globalVariable.getZabbixSession();
        Map<String, HostInfoDao> hostListMapByHostId = globalVariable.getHostListMapByHostId();
        Set<String> keySet = hostListMapByHostId.keySet();
        for(String eachKey : keySet){
            HostInfoDao hostInfoDao = hostListMapByHostId.get(eachKey);
            List<ItemInfoDao> itemInfoDao = hostInfoDao.getItemInfoDao();
            for(ItemInfoDao eachItemInfoDao : itemInfoDao){
                String itemid = eachItemInfoDao.getItemid();
                String graphid = eachItemInfoDao.getGraphid();
                downloadGraph(itemid,graphid);
            }
        }
    }

    @Async
    public void downloadGraph(String itemId,String graphId) throws ZabbixSessionTimeoutException, AuthenticationException, ZabbixConfigException, IOException {
        List<String> zabbixSession = (List<String>)globalVariable.getZabbixSession();
        byte[] picture = MyHttpUtil.getPicture(zabbixAddress + "/chart2.php?graphid=" + graphId, zabbixSession);
        if (picture.length<2048){
            loginService.getZbxSession();
            throw new ZabbixSessionTimeoutException("zabbix Session 过期,重新获取");
        }
        String path = "src/main/resources/static/image/" + itemId+".jpg";
        File file = new File(path);
        System.out.println(file.getAbsolutePath());
        FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));//打开输入流
        imageOutput.write(picture, 0, picture.length);//将byte写入硬盘
        imageOutput.close();
    }

    @Override
    public void getInterfacesToGeometry(){
        Map<String,List<Double>> map = new HashMap();
        try {
            //路径
            ClassPathResource classPathResource = new ClassPathResource(interfacesToGeometryPath);
            System.out.println(classPathResource.getPath());
            //读取文件信息
            String str = IOUtils.toString(new InputStreamReader(classPathResource.getInputStream(),"UTF-8"));
            //转换为Map对象
            map = JSONObject.parseObject(str, LinkedHashMap.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        globalVariable.setInterfacesToGeometry(map);

    }
}
