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
public class ZyyyAPIService extends Request2API{
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
    private static Map<String,String> ep;
//    static {
//        ep = new HashMap<String, String>();
//        ep.put("app_id",appId);
//        ep.put("app_key",appKey);
//        ep.put("client_mobile","");
//        ep.put("user_type",ZyyyConstant.APP_USER_TYPE);
//        ep.put("api_channel",ZyyyConstant.APP_API_CHANNLE);
//        ep.put("client_version",CLIENT_VERSION);
//    }
    //登录
    public void login(){
        String loginName="13758248635";
        String password="123456";
        JSONObject json = new JSONObject();
        json.put("phone",loginName);
        json.put("psw",password);

        ZyyyRequest request = initRequest();
        request.setApiName(ZyyyConstant.API_LOGIN);
        request.setParams(json);
        ZyyyResponse response = request2Server(JSON.toJSONString(request));
    }

    @Override
    public void faild() {

    }

    @Override
    public void success() {

    }
    private ZyyyRequest initRequest(){
        return new ZyyyRequest("",clientVersion,appId,appKey,
                ZyyyConstant.APP_API_CHANNLE,ZyyyConstant.APP_USER_TYPE);
    }

    public ZyyyResponse request2Server(String requestData){
        ZyyyResponse response = null;
        try {
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

    public static void main(String[] args) {
        String text="{\"return_code\":0,\"return_msg\":\"\",\"return_params\":{\"ret_code\":\"1\",\"ret_info\":\"密码输入过于频繁，请一小时后再试\"}}";
        JSONObject obj=JSON.parseObject(text);
    }
}
