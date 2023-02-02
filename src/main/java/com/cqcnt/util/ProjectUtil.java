package com.cqcnt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.cqcnt.config.GlobalVariable;
import com.cqcnt.dao.HostInfoDao;
import com.cqcnt.dao.dto.ItemGetDto;
import com.cqcnt.dao.form.ItemGetForm;
import com.cqcnt.service.APIDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ProjectUtil {

    public ItemGetDto itemSum(List<ItemGetForm> itemGetForms){
        Long lastValueSum = 0L;
        Long preValueSum = 0L;
        for(ItemGetForm eachItem : itemGetForms){
            lastValueSum += eachItem.getLastvalue();
            preValueSum += eachItem.getPrevvalue();
        }
        ItemGetDto itemGetDto = new ItemGetDto();
        itemGetDto = BeanCopyUtil.copyOne(itemGetForms.get(0),ItemGetDto.class);
        itemGetDto.setPrevvalue(preValueSum);
        itemGetDto.setLastvalue(lastValueSum);
        return itemGetDto;
    }

}
