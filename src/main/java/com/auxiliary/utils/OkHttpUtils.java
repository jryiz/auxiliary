package com.auxiliary.utils;

import okhttp3.*;

import java.io.IOException;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/4
 */
public class OkHttpUtils {

    public static void post(OkHttpClient okHttpClient,String url,String requestData,String... headers){
        RequestBody requestBody = new FormBody.Builder()
                .add("requestKey", "requestValue")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("","")
                .post(requestBody)
                .build();

    }

    public static void main(String[] args) {
        String url = "https://www.baidu.com/";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
