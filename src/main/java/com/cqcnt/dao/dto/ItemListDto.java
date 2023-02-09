package com.cqcnt.dao.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemListDto {
    private String label;
    private String value;
    List<ItemListDto> children;
}
