package com.auxiliary.model;

import com.alibaba.fastjson.JSONObject;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/7
 */
public interface Request2API {
     Response go(JSONObject obj);
//     void faild();
//     void success();
}
