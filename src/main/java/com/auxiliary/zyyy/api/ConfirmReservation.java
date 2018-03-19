package com.auxiliary.zyyy.api;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.zyyy.ZyyyAPIService;
import com.auxiliary.zyyy.ZyyyConstant;
import com.auxiliary.zyyy.model.ZyyyRequest;
import com.auxiliary.zyyy.model.ZyyyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 确认预约
 *
 * @auther ucmed Wenjun Choi
 * @create 2017/8/9
 */
@Component
public class ConfirmReservation extends ZyyyAPIService {
    @Override
    public ZyyyResponse go(JSONObject req) {
//        String phone = "";
//        String idCard = "";
//        String regId = "";
//        String regNo = "";
//        String docName = "";
//        String date = "2017-08-09 星期三 下午";
//        String scheduleId = "";
//        String deptName = "";
//        String user_name = "";
//        String sex = "";
//        String cardNo = "";
        ZyyyRequest request = new ZyyyRequest();
        request.setSessionId(req.getString("sessionId"));
        request.setParams(req.getJSONObject("params"));
        request.setApiName(ZyyyConstant.API_RESERVATION_CONFIRM);
        ZyyyResponse response = super.go(request);

        return response;
    }

    public ZyyyResponse test(){
        ZyyyResponse response = new ZyyyResponse();
        response.setReturnCode(200);
        response.setReturnParams(new JSONObject());
        return response;
    }
}
