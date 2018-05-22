package com.auxiliary;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.auxiliary.zyyy.ConfirmRunner;
import com.auxiliary.utils.DateUtil;
import com.auxiliary.zyyy.Main;
import com.auxiliary.zyyy.ZyyyCommon;
import com.auxiliary.zyyy.api.ConfirmReservation;
import com.auxiliary.zyyy.api.DoctorScheduleDetails;
import com.auxiliary.zyyy.api.Login;
import com.auxiliary.zyyy.model.UserMission;
import com.auxiliary.zyyy.model.ZyyyResponse;
import com.auxiliary.zyyy.model.ZyyyUser;
import com.auxiliary.zyyy.repository.UserMissionRepository;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {
    public static void main(String[] args) {
        JSONArray testList = JSONArray.parseArray("[{\"date\":\"20180529\",\"show_date\":\"2018-05-29 星期二\",\"doctor\":[{\"id\":\"1537\",\"name\":\"邬一军\",\"dept_name\":\"甲状腺疾病诊治中心病房(5-6)\",\"doct_rank\":\"主任医师\",\"doct_resume\":\"\",\"count\":15,\"schedulList\":[{\"id\":450851,\"plan_id\":3703,\"am_pm_flag\":\"1\",\"week_day\":\"星期二\",\"date\":\"2018-05-29 \",\"pre_date\":\"2018-05-29\",\"fee\":\"150\",\"enable\":\"1\",\"reg_id\":\"412241\",\"is_vary\":\"1\",\"am_cont\":15,\"no_arr\":[{\"schedule_id\":450837,\"reg_no\":\"36\"},{\"schedule_id\":450838,\"reg_no\":\"37\"},{\"schedule_id\":450839,\"reg_no\":\"38\"},{\"schedule_id\":450840,\"reg_no\":\"39\"},{\"schedule_id\":450841,\"reg_no\":\"40\"},{\"schedule_id\":450842,\"reg_no\":\"41\"},{\"schedule_id\":450843,\"reg_no\":\"42\"},{\"schedule_id\":450844,\"reg_no\":\"43\"},{\"schedule_id\":450845,\"reg_no\":\"44\"},{\"schedule_id\":450846,\"reg_no\":\"45\"},{\"schedule_id\":450847,\"reg_no\":\"46\"},{\"schedule_id\":450848,\"reg_no\":\"47\"},{\"schedule_id\":450849,\"reg_no\":\"48\"},{\"schedule_id\":450850,\"reg_no\":\"49\"},{\"schedule_id\":450851,\"reg_no\":\"50\"}]}]}]}]");
        System.out.println(testList);
        new Thread(new MissionRunner(testList)).start();
    }
    static class MissionRunner implements Runnable, Serializable {
        private static final long serialVersionUID = 1L;

        private final Logger LOG = Logger.getLogger(this.getClass());

        private ExecutorService confirmThreadPool = Executors.newCachedThreadPool();
        UserMissionRepository missionRepository;
        DoctorScheduleDetails doctorScheduleDetails;
        ConfirmReservation confirmReservation;
        UserMission mission;
        Login login;
        JSONObject req = new JSONObject();
        ZyyyUser user;
        JSONArray testList;
        public MissionRunner() {
        }
        public MissionRunner(JSONArray testList){
            this.testList = testList;
        }
        public MissionRunner(UserMissionRepository missionRepository, DoctorScheduleDetails doctorScheduleDetails,
                             ConfirmReservation confirmReservation, UserMission mission, Login login, ZyyyUser user) {
            super();
            this.missionRepository = missionRepository;
            this.doctorScheduleDetails = doctorScheduleDetails;
            this.confirmReservation = confirmReservation;
            this.mission = mission;
            this.login = login;
            this.user = user;
        }

        @Override
        public void run() {
            JSONObject params = new JSONObject();
//        if (isTimesToLogin(user))
//            return;

//            params.put("doctor_name", mission.getDoctorName());
//            params.put("dept_name", mission.getDeptName());
//            params.put("yuanqu_type", mission.getHospitalId());
//            req.put("sessionId", user.getSessionId());
//            req.put("params", params);
//            ZyyyResponse response = doctorScheduleDetails.go(req);
//            if (!response.isConnectedSucc() || !response.isSuccessful()) {
//                LOG.info("获取排班失败");
//                return;
//            }
//            params.clear();
//            JSONObject returnJson = response.getReturnParams();
//            JSONArray scheduleDateList = returnJson.getJSONArray("list");
            JSONArray scheduleDateList = testList;
            for (Object o : scheduleDateList) {
                JSONObject schedulDateObj = (JSONObject) o;
//                if (!mission.getMissionDate().equals(schedulDateObj.getString("date")))
//            if (!"20170818".equals(schedulDateObj.getString("date")))
//                    continue;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LOG.info(schedulDateObj.toJSONString());
                        confirmAppoint(schedulDateObj);
                    }
                }).start();

            }
        }
        private void confirmAppoint(JSONObject schedulDateObj) {
            JSONObject params = new JSONObject();
            JSONObject doctorSchedul = schedulDateObj.getJSONArray("doctor").getJSONObject(0);
            if (0 == doctorSchedul.getIntValue("count")) {
                LOG.info("无号源");
                return;
            }
            JSONArray doctorSchedulList = doctorSchedul.getJSONArray("schedulList");
            for (Object o : doctorSchedulList) {
                JSONObject schedulList = (JSONObject) o;
                if (1 != schedulList.getIntValue("is_vary") || 1 != schedulList.getIntValue("enable")
                        || 0 == schedulList.getIntValue("am_cont")) {
                    LOG.info("无号源或停诊");
                    continue;
                }
                String regId = schedulList.getString("reg_id");
                String regDate = schedulList.getString("date") + schedulList.getString("week_day")
                        + (schedulList.getIntValue("am_pm_flag") == 1 ? " 上午" : " 下午");

                params.put("reg_id", regId);
                params.put("date", regDate);
//                params.put("phone", user.getLoginName());
//                params.put("id_card", user.getIdCard());
                params.put("doct_name", doctorSchedul.getString("name"));
                params.put("dept_name", doctorSchedul.getString("dept_name"));
//                params.put("user_name", user.getUserName());
//                params.put("sex", user.getSex());
//                params.put("card_no", mission.getCardNo());

                JSONArray noArr = schedulList.getJSONArray("no_arr");
                ZyyyCommon.sort(noArr, "reg_no", null);
                for (Object obj : noArr) {
//                    confirmThreadPool.execute(new ConfirmRunner(
//                            params, confirmReservation, obj, regId, req, mission,missionRepository));
                    confirmThreadPool.execute(new TestRunner(obj));
                }
            }
        }
    }
    static class TestRunner implements Runnable {
        private JSONObject params;
        private Object obj;
        SimpleDateFormat sdf = new SimpleDateFormat("ss.SSS");
        TestRunner(){}
        TestRunner(JSONObject params){
            this.params = params;
        }
        TestRunner(Object obj){this.obj = obj;}
        @Override
        public void run() {
//            System.out.println(params.toJSONString());
            System.out.println(sdf.format(new Date())+"  "+obj.toString());
        }
    }
}
