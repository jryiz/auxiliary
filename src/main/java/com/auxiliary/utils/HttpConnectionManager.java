package com.auxiliary.utils;

import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

public class HttpConnectionManager {
	private static PoolingHttpClientConnectionManager cm = null;
//    private static  Properties props = null;
//	static{
//        try {
//            if(props == null){
//                Resource resource = new ClassPathResource("/configure.properties");
//                props = PropertiesLoaderUtils.loadProperties(resource);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//     }
	
   /** 
	 * 最大连接数 
	 */
	public static int maxTotalConnections;
	/** 
	 * 获取连接的最大等待时间 
	 */  
	public static int waitTimeout;
	/** 
	 * 每个路由最大连接数 
	 */  
	public  static int maxRouteConnections;
	/** 
	 * 连接超时时间 
	 */  
	public  static int connectTimeout;;
	/** 
	 * 读取超时时间 
	 */  
	public  static int readTimeout;
		  
    @PostConstruct
    public static void init() {//初始化
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        cm =new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        
        cm.setMaxTotal(maxTotalConnections);// // 将最大连接数增加
        cm.setDefaultMaxPerRoute(maxRouteConnections);//// 将每个路由基础的连接增加
    }

    public static <T> void config(T t) {
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectTimeout)
                .setConnectTimeout(waitTimeout)
                .setSocketTimeout(readTimeout)
                .build();
        ((HttpRequestBase) t).setConfig(requestConfig);
    }


    public static  CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();          
        
        /*CloseableHttpClient httpClient = HttpClients.createDefault();//如果不采用连接池就是这种方式获取连接*/
        return httpClient;
    }
}
