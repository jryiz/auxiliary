package com.auxiliary.utils;

import com.auxiliary.annotation.FieldValue;
import org.json.JSONObject;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/8
 */
public class ConvertUtils {
    /**
     * 解析json为对象
     * @param clazz
     * @param json
     * @param <T>
     * @return
     */
    public static <T> T JsonConvert(Class<T> clazz, JSONObject json){
        T returnClazz = null;
        try {
            returnClazz = clazz.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd:pds) {
                Method writeMethod = pd.getWriteMethod();
                //过滤没有set方法的属性如getClass();不然会转换失败
                if(null == writeMethod){
                    continue;
                }
                String itemName = pd.getName();
                Field f = clazz.getDeclaredField(itemName);
                FieldValue jsonField = f.getAnnotation(FieldValue.class);
                if (null == jsonField)
                    continue;
                //匹配类型
                Class<?> typeClass = f.getType();
                if(String.class.isAssignableFrom(typeClass)) {
                    writeMethod.invoke(returnClazz, json.optString(jsonField.value()));
                }
                else if (Integer.class.isAssignableFrom(typeClass)) {
                    writeMethod.invoke(returnClazz, json.optInt(jsonField.value()));
                }
                else if (Long.class.isAssignableFrom(typeClass)) {
                    writeMethod.invoke(returnClazz, json.optLong(jsonField.value()));
                }
                else if (Boolean.class.isAssignableFrom(typeClass)) {
                    writeMethod.invoke(returnClazz, json.optBoolean(jsonField.value()));
                }
                else if (Double.class.isAssignableFrom(typeClass)){
                    writeMethod.invoke(returnClazz,json.optDouble(jsonField.value()));
                }
                //日期格式需simpledateformat
//                else if (Date.class.isAssignableFrom(typeClass))
//                    pd.getWriteMethod().invoke(clazz,json.opt(jsonField.value()));
                else {
                    //object类
                    Object obj = json.opt(jsonField.value());
                    if (null != obj) {
                        writeMethod.invoke(returnClazz, obj);
                    }
                }
            }
            return returnClazz;
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnClazz;
    }
}
