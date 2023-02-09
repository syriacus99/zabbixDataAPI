package com.cqcnt.dao;

import lombok.Data;

@Data
public class DataVCityData {
    private Integer id;
    private Long value;
    private Integer sizeField = 5;
    private Integer colorField = 5;
    private Geometry geometry;
}
