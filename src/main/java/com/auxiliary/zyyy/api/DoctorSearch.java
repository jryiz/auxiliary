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
public class DoctorSearch extends ZyyyAPIService{
    @Override
    public ZyyyResponse go(JSONObject req) {
//        String doctorName = "邬一军";
//        String deptName = "胃肠甲状腺外科";
//        String hospitalId = "1";
//        JSONObject params = new JSONObject();
//        params.put("doctor_name", doctorName);
//        params.put("dept_name", deptName);
//        params.put("yuanqu_type", hospitalId);
        ZyyyRequest request = this.createRequest(req, ZyyyConstant.API_DOCTOR_SEARCH);
        return super.go(request);
//        if (!response.isSuccessful())
//            return response;
//        JSONObject rspJson = response.getReturnParams();
//        JSONArray list = rspJson.getJSONArray("list");
//        System.out.println(response.getReturnParams().toString());
    }
}
