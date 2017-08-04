package com.auxiliary.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/7/17
 */
@Component
public class MailPropertiesUtil extends Properties{
    private final Logger LOG = Logger.getLogger(this.getClass());
    private static Properties props;
//    private static Yaml yaml;
//    public static Map map;
    private static  MailPropertiesUtil INSTANCE;
    @Value("${mail.properties.path}")
    public static String path;

    static {
//        yaml = new Yaml();
        InputStream in;
        try {
            if (props == null) {
                Resource resource = new ClassPathResource("/mail.properties");
//                in = MailPropertiesUtil.class.getResourceAsStream("/mail.properties");
//                System.out.println(path);
//                props.load(in);
//                System.out.println(resource.getFile());//
                props = PropertiesLoaderUtils.loadProperties(resource);
//                map = (Map) yaml.load(MailPropertiesUtil.class.getClassLoader().getResourceAsStream("application-dev.yml"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void init(){
    }
    public static MailPropertiesUtil getInstance() throws Exception{
        if (INSTANCE == null) {
            synchronized (MailPropertiesUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MailPropertiesUtil();
                }
            }
        }
        return INSTANCE;
    }
    @Override
    public synchronized Object setProperty(String key, String value) {
        Object old = null;
        try {
            OutputStream os = new FileOutputStream(path);
            old = props.setProperty(key, value);
            props.store(os,"");
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return old;
    }

    @Override
    public String getProperty(String key) {
        return props.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
