package com.cqcnt.dao.form;

import lombok.Data;

@Data
public class ItemGetForm {
    private Integer itemid;

    private Integer hostid;

    private Long prevvalue;

    private Long lastvalue;

    private String name;
}
