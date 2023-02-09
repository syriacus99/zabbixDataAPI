package com.cqcnt.dao.dto;

import lombok.Data;

import java.util.List;

@Data
public class CascadeSelectorDto {
    private String value;
    private String label;
    List<CascadeSelectorDto> children;
}
