package com.cqcnt.dao;

import lombok.Data;

@Data
public class ItemInfoDao {
    private String itemid;
    private String name;
    private String lastvalue;
    private String prevvalue;
    private String graphid;
}
