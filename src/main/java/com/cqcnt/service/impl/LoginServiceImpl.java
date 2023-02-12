package com.cqcnt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cqcnt.config.GlobalVariable;
import com.cqcnt.exception.AuthenticationException;
import com.cqcnt.exception.GetTokenMethodError;
import com.cqcnt.exception.ZabbixConfigException;
import com.cqcnt.service.LoginService;
import com.cqcnt.util.MyHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.security.util.Length;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Value("${zabbix.username}")
    private String zabbixUserName;

    @Value("${zabbix.password}")
    private String zabbixPassword;

    @Value("${zabbix.address}")
    private String zabbixAddress;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private GlobalVariable globalVariable;

    @Override
    public void getToken() throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, ZabbixConfigException, AuthenticationException {

        // 用户名密码地址未设置
        if(zabbixUserName==null|zabbixPassword==null|zabbixAddress==null){
            throw new ZabbixConfigException("Zabbix配置错误");
        }
        String data = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"user.login\",\n" +
                "    \"params\": {\n" +
                "        \"user\": \""+zabbixUserName+"\",\n" +
                "        \"password\": \""+zabbixPassword+"\"\n" +
                "    },\n" +
                "    \"id\": 1,\n" +
                "    \"auth\": null\n" +
                "}";
        String body = MyHttpUtil.sendJsonPostRequest(zabbixAddress+"/api_jsonrpc.php",data,String.class);
        JSONObject responseBody = (JSONObject) JSONObject.parse(body);

        String token = null;
        token = (String) responseBody.get("result");
        if(token==null){
            throw new AuthenticationException("用户名密码错误");
        }
        //将token写入session
//        HttpSession session = request.getSession();
//        session.setAttribute("token",token);
        //将token写入GlobalVariable
        globalVariable.setToken(token);
        System.out.println(token);
    }

    @Override
    @Scheduled(cron ="* * */2 * * ?")
    public void getZbxSession() throws ZabbixConfigException, AuthenticationException {
        // 用户名密码地址未设置
        if(zabbixUserName==null|zabbixPassword==null|zabbixAddress==null){
            throw new ZabbixConfigException("Zabbix配置错误");
        }
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("name",zabbixUserName);
        map.add("password",zabbixPassword);
        map.add("autologin","1");
        map.add("enter","Sign in");
        Map<String, Object> response = MyHttpUtil.sendFormPostRequest(zabbixAddress+"/index.php",
                map);
        HttpHeaders header = (HttpHeaders) response.get("header");
        List<String> cookie = header.get("Set-Cookie");
        if(cookie==null){
            throw new AuthenticationException("用户名密码错误（获取zbxSession失败）");
        }
        globalVariable.setZabbixSession(cookie);
//        HttpSession session = request.getSession();
//        session.setAttribute("zbxSession",cookie);
    }

    @Override
    public void getAllHost(HttpServletRequest request) throws IntrospectionException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, GetTokenMethodError, ZabbixConfigException, AuthenticationException {
        HttpSession session = request.getSession();
        session.setAttribute("hostInfo",null);
        Object token = session.getAttribute("token");
        if (token == null ) {
            getToken();
        }
        token = session.getAttribute("token");

        String data = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"host.get\",\n" +
                "    \"params\": {\n" +
                "        \"output\": \"extend\",\n" +
                "        \"selectInterfaces\": [\n" +
                "            \"interfaceid\",\n" +
                "            \"ip\"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"id\": 1,\n" +
                "    \"auth\": \""+token.toString()+ "\"\n" +
                "}";
        String body = MyHttpUtil.sendJsonPostRequest(zabbixAddress+"/api_jsonrpc.php",data,String.class);
        JSONObject responseBody = (JSONObject) JSONObject.parse(body);
        List<JSONObject> result = null;
        JSONObject result2 = null;
        //若host只有一个时万一返回的json对象不是列表
        try {
            result = (List<JSONObject>) responseBody.get("result");
        }catch (Exception e){
            result2 = (JSONObject)responseBody.get("result");
        }
        if(result==null&result2==null){
            session.setAttribute("token",null);
            getAllHost(request);
        }
        if (session.getAttribute("hostInfo") == null){
            session.setAttribute("hostInfo",result==null?result2:result);
        }
        System.out.println(session.getAttribute("hostInfo"));
    }

}
