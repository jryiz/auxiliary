package com.auxiliary.zyyy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/10
 */
public class ZyyyCommon {
    public static final String ORDER_DESC = "DESC";
    public static final String ORDER_ASC = "ASC";
    public static void sort(JSONArray jsonArr,final String orderBy,String orderType) {
        List<JSONObject> list = jsonArr.toJavaList(JSONObject.class);
        Collections.sort(list, new Comparator<JSONObject>() {
           @Override
           public int compare(JSONObject o1, JSONObject o2) {
               if (ORDER_DESC.equals(orderType))
                    return o2.getIntValue(orderBy)-o1.getIntValue(orderBy);
               return o1.getIntValue(orderBy)-o1.getIntValue(orderBy);
           }
        });
        jsonArr.clear();
        jsonArr.addAll(list);
    }
    public static void sort2(JSONArray jsonArr,final String orderBy,String orderType) {
        List<JSONObject> list = jsonArr.toJavaList(JSONObject.class);
        list.sort((JSONObject o1,JSONObject o2) -> o1.getString(orderBy).compareTo(o2.getString(orderBy)));
        jsonArr.clear();
        jsonArr.addAll(list);
    }
    public static void main(String[] args) {
        String jsonStr = "{\"return_code\":0,\"return_msg\":\"\",\"return_params\":{\"ret_code\":0,\"list\":[{\"date\":\"20170811\",\"show_date\":\"2017-08-11 星期五\",\"doctor\":[{\"id\":\"1631\",\"name\":\"卢震亚\",\"dept_name\":\"健康管理中心\",\"doct_rank\":\"副主任医师\",\"doct_resume\":\"。\",\"count\":8,\"schedulList\":[{\"id\":273642,\"plan_id\":4473,\"am_pm_flag\":\"1\",\"week_day\":\"星期五\",\"date\":\"2017-08-11 \",\"pre_date\":\"2017-08-11\",\"fee\":\"50\",\"enable\":\"1\",\"reg_id\":\"715200\",\"is_vary\":\"1\",\"am_cont\":8,\"no_arr\":[{\"schedule_id\":273634,\"reg_no\":\"4\"},{\"schedule_id\":273635,\"reg_no\":\"1\"},{\"schedule_id\":273636,\"reg_no\":\"6\"},{\"schedule_id\":273637,\"reg_no\":\"3\"},{\"schedule_id\":273639,\"reg_no\":\"8\"},{\"schedule_id\":273640,\"reg_no\":\"7\"},{\"schedule_id\":273641,\"reg_no\":\"9\"},{\"schedule_id\":273642,\"reg_no\":\"10\"}]}]}]}," +
                "{\"date\":\"20170814\",\"show_date\":\"2017-08-14 星期一\",\"doctor\":[{\"id\":\"1631\",\"name\":\"卢震亚\",\"dept_name\":\"健康管理中心\",\"doct_rank\":\"副主任医师\",\"doct_resume\":\"。\",\"count\":0,\"schedulList\":[{\"id\":273910,\"plan_id\":4117,\"am_pm_flag\":\"1\",\"week_day\":\"星期一\",\"date\":\"2017-08-14 \",\"pre_date\":\"2017-08-14\",\"fee\":\"50\",\"enable\":\"0\",\"reg_id\":\"786218\",\"is_vary\":\"1\",\"am_cont\":0,\"no_arr\":[]}]}]},{\"date\":\"20170815\",\"show_date\":\"2017-08-15 星期二\",\"doctor\":[{\"id\":\"1631\",\"name\":\"卢震亚\",\"dept_name\":\"健康管理中心\",\"doct_rank\":\"副主任医师\",\"doct_resume\":\"。\",\"count\":14,\"schedulList\":[{\"id\":274710,\"plan_id\":4237,\"am_pm_flag\":\"1\",\"week_day\":\"星期二\",\"date\":\"2017-08-15 13:15-13:45\",\"pre_date\":\"2017-08-15\",\"fee\":\"50\",\"enable\":\"1\",\"reg_id\":\"414519\",\"is_vary\":\"1\",\"am_cont\":8,\"no_arr\":[{\"schedule_id\":274696,\"reg_no\":\"5\"},{\"schedule_id\":274697,\"reg_no\":\"7\"},{\"schedule_id\":274698,\"reg_no\":\"8\"},{\"schedule_id\":274702,\"reg_no\":\"6\"},{\"schedule_id\":274707,\"reg_no\":\"1\"},{\"schedule_id\":274708,\"reg_no\":\"2\"},{\"schedule_id\":274709,\"reg_no\":\"3\"},{\"schedule_id\":274710,\"reg_no\":\"4\"}]},{\"id\":274706,\"plan_id\":4237,\"am_pm_flag\":\"2\",\"week_day\":\"星期二\",\"date\":\"2017-08-15 09:10-09:40\",\"pre_date\":\"2017-08-15\",\"fee\":\"50\",\"enable\":\"1\",\"reg_id\":\"424668\",\"is_vary\":\"1\",\"am_cont\":6,\"no_arr\":[{\"schedule_id\":274700,\"reg_no\":\"11\"},{\"schedule_id\":274701,\"reg_no\":\"12\"},{\"schedule_id\":274703,\"reg_no\":\"10\"},{\"schedule_id\":274704,\"reg_no\":\"13\"},{\"schedule_id\":274705,\"reg_no\":\"14\"},{\"schedule_id\":274706,\"reg_no\":\"15\"}]}]}]}],\"total_count\":31,\"ret_info\":\"成功\",\"page_count\":1}}";
        JSONObject json = JSON.parseObject(jsonStr);
        JSONArray jsonArr = json.getJSONObject("return_params").getJSONArray("list").getJSONObject(0).getJSONArray("doctor").getJSONObject(0).getJSONArray("schedulList").getJSONObject(0).getJSONArray("no_arr");
       String orderBy = "reg_no";
        System.out.println(jsonArr.toString());
        Comparator<JSONObject> compartor = (JSONObject o1,JSONObject o2) -> o1.getString(orderBy).compareTo(o2.getString(orderBy));
//        jsonArr.sort(null);
//        sort(jsonArr,"reg_no",ORDER_DESC);
        sort2(jsonArr,"reg_no",ORDER_DESC);
        System.out.println(jsonArr.toString());
    }
}
