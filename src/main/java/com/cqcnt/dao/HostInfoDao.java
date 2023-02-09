package com.cqcnt.dao;

import lombok.Data;

import java.util.List;

@Data
public class HostInfoDao {
    private String name;
    private Integer hostid;
    private List<String> interfaces;
    private List<ItemInfoDao> itemInfoDao;
    private String fullName;
    private String city;
    private List<Double> geometry;
}
