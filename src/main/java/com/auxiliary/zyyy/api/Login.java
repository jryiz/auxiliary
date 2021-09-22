package com.auxiliary.zyyy.api;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.zyyy.ZyyyAPIService;
import com.auxiliary.zyyy.ZyyyConstant;
import com.auxiliary.zyyy.model.ZyyyRequest;
import com.auxiliary.zyyy.model.ZyyyResponse;
import com.auxiliary.zyyy.model.ZyyyUser;
import com.auxiliary.zyyy.repository.ZyyyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/9
 * 登录获取session
 * return_code=403重新获取session
 */
@Component
public class Login extends ZyyyAPIService {
    @Autowired
    ZyyyUserRepository userRepository;

    public ZyyyResponse go(JSONObject req) {
//        String loginName="13758248635";
//        String password="Jean123";
//        JSONObject json = new JSONObject();
//        json.put("phone",loginName);
//        json.put("psw",password);
        ZyyyRequest request = new ZyyyRequest();
        request.setParams(req.getJSONObject("params"));
        request.setApiName(ZyyyConstant.API_DOCTOR_SCHEDULE);
        ZyyyResponse response = super.go(request);
        return response;
    }

    public boolean loginAndSave(ZyyyUser user) {
//        ZyyyUser anotherUser = userRepository.findByLoginNameAndIsDelete(user.getLoginName(),"0");
//        if (anotherUser != null)
//            user = anotherUser;
        JSONObject params = new JSONObject();
        params.put("phone", user.getLoginName());
        params.put("psw", user.getPassword());
        params.put("device","9b30574e18d5b7b6fda1d91c158465d82b2778b91ae3b2b850fb72b92e6b4497");
        ZyyyRequest request = new ZyyyRequest();
        request.setParams(params);
        request.setApiName(ZyyyConstant.API_LOGIN);
        ZyyyResponse response = super.go(request);
        if (!response.isSuccessful())
            return false;
        JSONObject userModel = response.getReturnParams().getJSONObject("user_model");
        JSONObject model = userModel.getJSONObject("model");
        String sessionId = userModel.getString("session_id");
        String userName = model.getString("real_name");
        String sex = model.getString("sex");
        String idCard = model.getString("id_card");
        user.setSessionId(sessionId);
        user.setIdCard(idCard);
        user.setSex(sex);
        user.setUserName(userName);
        userRepository.save(user);
        return true;
    }

    public boolean testSession(String sessionId) {
        ZyyyRequest request = new ZyyyRequest();
        request.setSessionId(sessionId);
        request.setApiName(ZyyyConstant.API_RESERVATION_HISTORY);
        request.setParams(new JSONObject().fluentPut("page_size",1));
        ZyyyResponse response = super.go(request);
        return response.isConnectedSucc();
    }
}
