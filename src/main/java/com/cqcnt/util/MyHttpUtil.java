package com.cqcnt.util;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class MyHttpUtil {

    static public <T> T sendJsonPostRequest(String url,String jsonData,Class<T> clazz){

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();
        //请求头
        HttpHeaders headers = new HttpHeaders();
        //设置返回媒体数据类型
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Content-Type","application/json");
        // post请求的请求体
        HttpEntity<String> formEntity = new HttpEntity<>(jsonData, headers);
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(url,formEntity,clazz);
        return responseEntity.getBody();
    }

    static public Map<String,Object> sendFormPostRequest(String url, MultiValueMap<String,String> formData){
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();
        //请求头
        HttpHeaders headers = new HttpHeaders();
        //设置返回媒体数据类型
        headers.add("Accept", "*/*");
        headers.add("Content-Type","multipart/form-data");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");
        headers.add("Connection","keep-alive");
        headers.add("Accept-Encoding","keep-alive");
        // post请求的请求体
        HttpEntity<MultiValueMap> formEntity = new HttpEntity<>(formData, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,formEntity, String.class);
        HttpHeaders responseHeader = responseEntity.getHeaders();
        String body = responseEntity.getBody();
        Map<String,Object> response = new HashMap<>();
        response.put("header",responseHeader);
        response.put("body",body);
        return response;
    }

    static public byte[] getPicture(String url){
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();
        byte[] img = restTemplate.getForObject(url, byte[].class);
        return img;
    }

    static public byte[] getPicture(String url,Object cookie){
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();
        HttpHeaders headers = new HttpHeaders();
        //设置返回媒体数据类型
        headers.add("Cookie",cookie.toString());
        HttpEntity requestEntity = new HttpEntity(headers);
        ResponseEntity<byte[]> img = restTemplate.getForEntity(url,byte[].class,requestEntity);
        //byte[] img = restTemplate.getForObject(url, byte[].class);
        return img.getBody();
    }
}
