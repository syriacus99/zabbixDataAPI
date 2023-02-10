package com.cqcnt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.cqcnt.config.GlobalVariable;
import com.cqcnt.dao.HostInfoDao;
import com.cqcnt.dao.ItemInfoDao;
import com.cqcnt.dao.dto.ItemGetDto;
import com.cqcnt.dao.form.ItemGetForm;
import com.cqcnt.service.APIDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

@Component
public class ProjectUtil {

    public Map<String, Long[]> itemSum(HostInfoDao hostInfoDao) {
        List<String> interfaces = hostInfoDao.getInterfaces();
        Map<String, Long[]> interfacesData = new HashMap<>();
        for (String eachInterface : interfaces) {
            Long[] l = new Long[2];
            l[0] = 0L;
            l[1] = 0L;
            interfacesData.put(eachInterface, l);
        }
        Set<String> keySet = interfacesData.keySet();
        List<ItemInfoDao> itemInfoDao = hostInfoDao.getItemInfoDao();
        for (ItemInfoDao eachItem : itemInfoDao) {
            String name = eachItem.getName();
            for (String eachKey : keySet) {
                if (name.contains(eachKey)) {
                    Long[] longs = interfacesData.get(eachKey);
                    longs[0] += Long.parseLong(eachItem.getPrevvalue());
                    longs[1] += Long.parseLong(eachItem.getLastvalue());
                    interfacesData.put(eachKey, longs);
                }
            }
        }
        return interfacesData;
    }
}
