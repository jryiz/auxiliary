package com.auxiliary;

import com.auxiliary.http.HttpAPIService;
import com.auxiliary.utils.HttpConnectionManager;
import com.auxiliary.zyyy.Main;
import com.auxiliary.zyyy.api.DoctorScheduleDetails;
import com.auxiliary.zyyy.api.Login;
import com.auxiliary.zyyy.mapper.ZyyyMapper;
import com.auxiliary.zyyy.model.ZyyyUser;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/4
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuxiliaryApplication.class)
@Configuration
public class httpTest {
    
    @Value("${zyyy.api-url}")
    public String zyyyUrl;

    private static final Logger LOG = Logger.getLogger("httpTest");

    @Value("${api.server.url}")
    private static String url;

    @Autowired
    private HttpAPIService httpAPIService;

    @Autowired
    private OkHttpClient okHttpClient;

    public String request(JSONObject obj) {
        CloseableHttpResponse response=null;
        CloseableHttpClient httpClient1= HttpConnectionManager.getHttpClient();
        String json=null;
        HttpPost httppost = null;
        try {
            obj.put("app_id", "zsly");
            obj.put("app_key", "ZW5sNWVWOWhibVJ5YjJsaw==");
            obj.put("coder", "enl5eVlYQndYMnRsZVE9PQ");
            httppost = new HttpPost(url);
            httppost.addHeader("Content-type",
                    "application/x-www-form-urlencoded;charset=utf-8");
            httppost
                    .addHeader(
                            "Accept",
                            "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            // 创建参数队列
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("requestData", obj.toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nvps, Charset.forName("utf-8")));
            HttpConnectionManager.config(httppost);//配置请求的超时设置

            response = httpClient1.execute(httppost);
            if(response!=null&& response.getStatusLine().getStatusCode() == 200){
                InputStream in=response.getEntity().getContent();
                //json=IOUtils.toString(in);
                json = IOUtils.toString(in,"UTF-8");
                in.close();
                return json;
            }
            LOG.info("web app http请求状态不是200 强制终止http请求------->");
            httppost.abort();
            return null;
        } catch (UnsupportedOperationException e) {
            httppost.abort();
            LOG.error(e.getMessage()+" http请求UnsupportedOperationException");
        } catch (IOException e) {
            httppost.abort();
            LOG.error(e.getMessage()+" http请求IOException");
        } catch(Exception e) {
            httppost.abort();
            LOG.error(e.getMessage()+" http请求Exception");
        } finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    httppost.abort();
                    LOG.error(e.getMessage()+" finallay  http请求IOException");
                }
            }
        }
        return json;
    }

    @Test
    public void requestTest() throws Exception {
//        String str = httpAPIService.doGet("http://www.baidu.com" );
//        System.out.println(str);
        String url = "https://www.baidu.com/";
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        System.out.println(okHttpClient.toString());
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    @Autowired
//    ZyyyAPIService zyyyAPIService;
    @Autowired
    Login login;
    @Autowired
    DoctorScheduleDetails doctorScheduleDetails;

    @Autowired
    Main main;

    @Test
    public void loginTest() throws Exception {
//        main.mainTask();
        main.testSession2UserTask();
    }

    @Autowired
    ZyyyMapper zyyyMapper;

    @Test
    public void mapperTest(){
        System.out.println("xxxx");
        List<ZyyyUser> list = zyyyMapper.findMissionUserByDate("20170818");
    }
}
