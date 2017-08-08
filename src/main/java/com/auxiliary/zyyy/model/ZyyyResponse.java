package com.auxiliary.zyyy.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.auxiliary.annotation.FieldValue;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/8
 */
public class ZyyyResponse {
    @JSONField(name = "return_code")
    private Integer returnCode;
    @JSONField(name = "return_msg")
    private String returnMsg;
    @JSONField(name = "return_params")
    private JSONObject returnParams;

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public JSONObject getReturnParams() {
        return returnParams;
    }

    public void setReturnParams(JSONObject returnParams) {
        this.returnParams = returnParams;
    }
}
