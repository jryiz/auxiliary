package com.auxiliary.zyyy.api;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.zyyy.ZyyyAPIService;
import com.auxiliary.zyyy.ZyyyConstant;
import com.auxiliary.zyyy.model.ZyyyRequest;
import com.auxiliary.zyyy.model.ZyyyResponse;
import org.springframework.stereotype.Component;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/9
 * 获取具体医生排班
 */
@Component
public class RemainNum extends ZyyyAPIService{
    @Override
    public ZyyyResponse go(JSONObject req) {
        ZyyyRequest request = this.createRequest(req, ZyyyConstant.API_REMAIN_NUM);
        return super.go(request);
    }
}
