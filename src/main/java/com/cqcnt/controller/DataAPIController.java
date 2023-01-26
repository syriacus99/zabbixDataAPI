package com.cqcnt.controller;

import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.GetResultException;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.service.APIDataService;
import com.cqcnt.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
public class DataAPIController {
    @Resource
    private APIDataService apiDataService;

    @RequestMapping("/getOneItemData")
    private Result getOneItemData(HttpServletRequest request, Integer itemData) throws GetResultException {
        Result result = apiDataService.getOneItemsData(request,itemData);
        return result;
    }

    @RequestMapping("/getGraph")
    private byte[] getGraph(HttpServletRequest request, Integer graphId) throws AuthenticationException, ZabbixConfigException {
        return apiDataService.getGraph(request,graphId);
    }
}
