package com.cqcnt.dao.form;

import lombok.Data;

@Data
public class GetItemsInfoByHostIdForm {
    private String itemid;
    private String name;
    private String tag;
    private String lastvalue;
    private String prevvalue;
}
