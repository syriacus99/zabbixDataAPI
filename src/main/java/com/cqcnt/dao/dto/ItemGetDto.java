package com.cqcnt.dao.dto;

import lombok.Data;

@Data
public class ItemGetDto {
    private Integer itemid;

    private Integer hostid;

    private Long prevvalue;

    private Long lastvalue;

    private String name;
}
