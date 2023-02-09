package com.cqcnt.controller;

import com.cqcnt.dao.HostInfoDao;
import com.cqcnt.dao.ItemInfoDao;
import com.cqcnt.dao.dto.CascadeSelectorDto;
import com.cqcnt.dao.dto.CityDataDto;
import com.cqcnt.dao.form.GetItemsInfoByHostIdForm;
import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.CqcntException;
import com.cqcnt.exception.GetResultException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.service.APIDataService;
import com.cqcnt.util.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class DataAPIController {
    @Resource
    private APIDataService apiDataService;

    @RequestMapping("/getOneItemData")
    private Result getOneItemData(Integer itemData) throws GetResultException {
        Result result = apiDataService.getOneItemsData(itemData);
        return result;
    }

    @RequestMapping(value="/getGraph",produces = MediaType.IMAGE_JPEG_VALUE)
    private byte[] getGraph(HttpServletRequest request, Integer graphId) throws AuthenticationException, ZabbixConfigException, IOException {
        return apiDataService.getGraph(request,graphId);
    }

    @RequestMapping("/getAllItemsInfo")
    private Result getAllItemsInfo(Integer hostId){
        return null;
    }

    @RequestMapping("/getHostByCity")
    private Result getHostByCity(String cityId) throws CqcntException {
        List<HostInfoDao> hostByCity = apiDataService.getHostByCity(cityId);
        List<CascadeSelectorDto> cascadeSelectorDtoList = new ArrayList<>();
        int id = -1;
        for(HostInfoDao eachHostInfoDao : hostByCity){
            CascadeSelectorDto newC = new CascadeSelectorDto();
            newC.setLabel(eachHostInfoDao.getName());
            newC.setValue(id+"");
            List<CascadeSelectorDto> children = new ArrayList<>();
            List<ItemInfoDao> itemInfoDao = eachHostInfoDao.getItemInfoDao();
            for(ItemInfoDao eachItem : itemInfoDao){
                CascadeSelectorDto newChildrenC = new CascadeSelectorDto();
                newChildrenC.setLabel(eachItem.getName());
                newChildrenC.setValue(eachItem.getItemid());
                children.add(newChildrenC);
            }
            newC.setChildren(children);
            cascadeSelectorDtoList.add(newC);
            id--;
        }
        return Result.success(200,"success",cascadeSelectorDtoList);
    }

    public Result getOneCityItemsData(String cityCode){
        CityDataDto oneCityItemsData = apiDataService.getOneCityItemsData(cityCode);
        return Result.success(200,"successful",oneCityItemsData);
    }

    @RequestMapping(value="/test")
    private Result test(String code) {
        System.out.println("111111");
        List<GetItemsInfoByHostIdForm> itemsInfoByHostId = apiDataService.getItemsInfoByHostId(10529);
        System.out.println(itemsInfoByHostId);
        return Result.success(200,"11111",itemsInfoByHostId);
    }
}
