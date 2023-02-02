package com.cqcnt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cqcnt.config.GlobalVariable;
import com.cqcnt.dao.HostInfoDao;
import com.cqcnt.service.APIDataService;
import com.cqcnt.service.ProjectInitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

@Service
public class ProjectInitServiceImpl implements ProjectInitService {

    @Value("${zabbix.config}")
    private String zabbixHostConfigPath;
    @Resource
    private GlobalVariable globalVariable;

    @Resource
    private APIDataService apiDataService;

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
        Map<String, HostInfoDao> hostListMap = globalVariable.getHostListMap();
        System.out.println(globalVariable.getToken());
        for (HostInfoDao eachHost : hostInfoDaos){
            String hostId = apiDataService.getHostIdByHostName(eachHost.getName());
            if (hostId != null){
                eachHost.setHostid(Integer.valueOf(hostId));
                hostListMap.put(hostId,eachHost);
            }
        }
    }

    @Override
    public void getItemsInfo() {

    }


}
