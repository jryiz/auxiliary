package com.auxiliary.zyyy.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.auxiliary.annotation.FieldValue;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/7
 */
@Component
public class ZyyyRequest {

    @FieldValue("session_id")
    @JSONField(name = "session_id")
    private String sessionId;

    @FieldValue("params")
    @JSONField(name = "params")
    private JSONObject params;

    @FieldValue("api_name")
    @JSONField(name = "api_name")
    private String apiName;

    @FieldValue("client_mobile")
    @JSONField(name = "client_mobile")
    private String clientMobile;

    @FieldValue("client_version")
    @JSONField(name = "client_version")
    private String clientVersion;

    @FieldValue("app_id")
    @JSONField(name = "app_id")
    private String appId;

    @FieldValue("app_key")
    @JSONField(name = "app_key")
    private String appKey;

    @FieldValue("app_Channel")
    @JSONField(name = "app_Channel")
    private String apiChannel;

    @FieldValue("user_type")
    @JSONField(name = "user_type")
    private String userType;

    public ZyyyRequest() {
    }

    public ZyyyRequest(String clientMobile, String clientVersion, String appId,
                       String appKey, String apiChannel, String userType) {
        this.clientMobile = clientMobile;
        this.clientVersion = clientVersion;
        this.appId = appId;
        this.appKey = appKey;
        this.apiChannel = apiChannel;
        this.userType = userType;
    }

    public ZyyyRequest(Map<String,String> ep){
        super();
        this.clientMobile = ep.get("client_mobile");
        this.clientVersion = ep.get("client_version");
        this.appId = ep.get("app_id");
        this.appKey = ep.get("app_key");
        this.apiChannel = ep.get("api_channel");
        this.userType = ep.get("user_type");

    }

    public String getClientMobile() {
        return clientMobile;
    }

    public void setClientMobile(String clientMobile) {
        this.clientMobile = clientMobile;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getApiChannel() {
        return apiChannel;
    }

    public void setApiChannel(String apiChannel) {
        this.apiChannel = apiChannel;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }
}
