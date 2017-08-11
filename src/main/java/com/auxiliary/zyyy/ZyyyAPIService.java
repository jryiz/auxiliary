package com.auxiliary.zyyy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.model.Request2API;
import com.auxiliary.zyyy.model.ZyyyRequest;
import com.auxiliary.zyyy.model.ZyyyResponse;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/7
 */
@Component
public abstract class ZyyyAPIService implements Request2API{
    private final Logger LOG = Logger.getLogger(this.getClass());
    @Autowired
    OkHttpClient okHttpClient;
    @Value("${zyyy.app.id}")
    private String appId;
    @Value("${zyyy.app.key}")
    private String appKey;
    @Value("${zyyy.api-url}")
    private String apiUrl;
    @Value("${zyyy.app.version}")
    private String clientVersion;

    private static String sessionId;

    public ZyyyResponse go(JSONObject json,String apiName){
        ZyyyRequest request = initRequest();
        request.setApiName(apiName);
        request.setParams(json);
        return request2Server(JSON.toJSONString(request));
    }
    public ZyyyResponse go(ZyyyRequest request){
        initRequest(request);
        return request2Server(JSON.toJSONString(request));
//        return test();
    }
    //登录
    public void login(){
        String loginName="13758248635";
        String password="Jean123";
        JSONObject json = new JSONObject();
        json.put("phone",loginName);
        json.put("psw",password);

        ZyyyRequest request = initRequest();
        request.setApiName(ZyyyConstant.API_LOGIN);
        request.setParams(json);
        ZyyyResponse response = request2Server(JSON.toJSONString(request));
        if (response.isSuccessful()){
            JSONObject returnJson = response.getReturnParams();
//            String sessionId = returnJson.getString("session_id");
            sessionId = "d948a3927f8d32fd5b5d01b88542aedc";
        }
    }
    private boolean isLogin(){
        if (null == this.sessionId)
            return false;
        return true;
    }

    private ZyyyRequest initRequest(){
        return new ZyyyRequest("",clientVersion,appId,appKey,
                ZyyyConstant.APP_API_CHANNLE,ZyyyConstant.APP_USER_TYPE);
    }
    private void initRequest(ZyyyRequest request){
        request.setAppId(appId);
        request.setAppKey(appKey);
        request.setClientMobile("");
        request.setClientVersion(clientVersion);
        request.setUserType(ZyyyConstant.APP_USER_TYPE);
        request.setApiChannel(ZyyyConstant.APP_API_CHANNLE);
    }
    public ZyyyResponse request2Server(String requestData){
        ZyyyResponse response = null;
        try {
            LOG.info("请求数据:" + requestData);
            RequestBody requestBody = new FormBody.Builder()
                    .add("requestData", requestData)
                    .build();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .addHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                    .addHeader("Accept",
                            "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5")
                    .post(requestBody)
                    .build();
            Response rsp = okHttpClient.newCall(request).execute();
            if (rsp.isSuccessful()) {
                LOG.info("返回成功");
                String rspContent = rsp.body().string();
                LOG.info("返回数据：" + rspContent);
                response = JSON.parseObject(rspContent, ZyyyResponse.class);
            }
        }catch (Exception e){
            LOG.error("请求服务器出错",e);
        }
        return response;
    }

    public ZyyyResponse test(){
        String url = "https://www.baidu.com/";
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        System.out.println(okHttpClient.toString());
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
