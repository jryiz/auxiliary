package com.auxiliary;

import com.auxiliary.http.HttpAPIService;
import com.auxiliary.utils.HttpConnectionManager;
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
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
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
@Component
public class httpTest {

    private static final Logger LOG = Logger.getLogger("httpTest");
    @Value("api.server.url")
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
