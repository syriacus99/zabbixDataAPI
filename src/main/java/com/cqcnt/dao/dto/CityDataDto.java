package com.cqcnt.dao.dto;

import com.cqcnt.dao.DataVCityData;
import com.cqcnt.dao.ItemInfoDao;
import lombok.Data;

import java.util.List;

@Data
public class CityDataDto {
    List<DataVCityData> dataVCityData;
}
