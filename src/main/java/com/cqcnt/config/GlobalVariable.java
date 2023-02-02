package com.cqcnt.config;

import com.cqcnt.dao.HostInfoDao;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class GlobalVariable {
    private static GlobalVariable globalVariable = new GlobalVariable();

    private Map<String, HostInfoDao> hostListMap = new HashMap<>();

    private Map<String, HostInfoDao> hostListMapNameKey = new HashMap<>();

    private Object token;

    private Object zabbixSession;

    private GlobalVariable(){}

    public static GlobalVariable getInstance(){
        return globalVariable;
    }
}
