package com.auxiliary.utils;

import okhttp3.*;
import okio.BufferedSink;

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
//        String url = "https://www.baidu.com/";
//        String url = "https://open-project-dev.zwjk.com/export/scyUsers/0fdc9230-4327-40ec-8a46-49122168bbb1/patientVisits";
        String url = "https://open-project-dev.zwjk.com/rubik/api/patient/hospital/5e6d53ff-b5f2-4812-9d2e-a92f2ce8546a/getScyUserId";
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),"{\n" +
                "\t\"idCard\": \"\",\"patientIdCardNumber\": \"\",\"phoneNumber\": \"13758248635\"\n" +
                "}");
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept","application/json")
                .addHeader("Content-Type","application/json")
                .post(requestBody)
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
