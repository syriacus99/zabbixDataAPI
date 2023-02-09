package com.cqcnt.config;

import com.cqcnt.dao.HostInfoDao;
import com.cqcnt.dao.ItemInfoDao;
import com.cqcnt.dao.form.GetItemsInfoByHostIdForm;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Component
public class GlobalVariable {
    private static GlobalVariable globalVariable = new GlobalVariable();

    private Map<String, HostInfoDao> hostListMapByHostId = new HashMap<>();

    private Map<String, Set<String>> hostListMapByCity = new HashMap<>();

    private Map<String,List<Double>> interfacesToGeometry = new HashMap<>();

    //private Map<String, List<ItemInfoDao>> itemsDataMapByHostId = new HashMap<>();

    private Object token;

    private Object zabbixSession;

    private GlobalVariable(){}

    public static GlobalVariable getInstance(){
        return globalVariable;
    }
}
