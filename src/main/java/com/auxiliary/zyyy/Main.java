package com.auxiliary.zyyy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.utils.DateUtil;
import com.auxiliary.zyyy.api.ConfirmReservation;
import com.auxiliary.zyyy.api.DoctorScheduleDetails;
import com.auxiliary.zyyy.api.Login;
import com.auxiliary.zyyy.model.UserMission;
import com.auxiliary.zyyy.model.ZyyyResponse;
import com.auxiliary.zyyy.model.ZyyyUser;
import com.auxiliary.zyyy.repository.UserMissionRepository;
import com.auxiliary.zyyy.repository.ZyyyUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * return_code=3 服务器挂了
 *
 * @auther ucmed Wenjun Choi
 * @create 2017/8/9
 */
@Component
public class Main {
    private final Logger LOG = Logger.getLogger(this.getClass());
    @Autowired
    Login login;
    @Autowired
    DoctorScheduleDetails doctorScheduleDetails;
    @Autowired
    ConfirmReservation confirmReservation;

    @Autowired
    UserMissionRepository missionRepository;
    @Autowired
    ZyyyUserRepository userRepository;

    public static final Integer POOL_SIZE = 5;

    ExecutorService missionThreadPool = Executors.newFixedThreadPool(POOL_SIZE);
    ExecutorService confirmThreadPool = Executors.newCachedThreadPool();

    public void mainTask() {
        SimpleDateFormat sdf = new SimpleDateFormat(ZyyyConstant.DEFAULT_DATE_FORMAT);
        List<UserMission> list = missionRepository.findListByMissionDateAndIsDelete(DateUtil.getDateByDays(7), "0");
        for (UserMission mission : list) {
            ZyyyUser user = userRepository.findOne(mission.getUserId());

            missionThreadPool.execute(new MissionRunner(userRepository,
                    doctorScheduleDetails, confirmReservation, mission, login,user));
        }
    }

    public void testSession2Accout(){
        List<UserMission> readyMissionList =
                missionRepository.findListByMissionDateAndIsDelete(DateUtil.getDateByDays(7), "0");
    }
}

class MissionRunner implements Runnable,Serializable {
    private static final long serialVersionUID = 1L;

    private final Logger LOG = Logger.getLogger(this.getClass());

    private static ExecutorService confirmThreadPool = Executors.newCachedThreadPool();
    ZyyyUserRepository userRepository;
    DoctorScheduleDetails doctorScheduleDetails;
    ConfirmReservation confirmReservation;
    UserMission mission;
    Login login;
    JSONObject req = new JSONObject();
    ZyyyUser user;
    public MissionRunner() {
    }

    public MissionRunner(ZyyyUserRepository userRepository, DoctorScheduleDetails doctorScheduleDetails,
                         ConfirmReservation confirmReservation, UserMission mission, Login login,ZyyyUser user) {
        super();
        this.userRepository = userRepository;
        this.doctorScheduleDetails = doctorScheduleDetails;
        this.confirmReservation = confirmReservation;
        this.mission = mission;
        this.login = login;
        this.user = user;
    }

    @Override
    public void run() {
        JSONObject params = new JSONObject();
//        ZyyyUser user = userRepository.findOne(mission.getUserId());

        if (isTimesToLogin(user))
            return;

        params.put("doctor_name", mission.getDoctorName());
        params.put("dept_name", mission.getDeptName());
        params.put("yuanqu_type", mission.getHospitalId());
        req.put("sessionId", user.getSessionId());
        req.put("params", params);
        ZyyyResponse response = doctorScheduleDetails.go(req);
        if (!response.isConnectedSucc() || !response.isSuccessful()) {
            LOG.info("获取排班失败");
            return;
        }
        params.clear();
        JSONObject returnJson = response.getReturnParams();
        JSONArray scheduleDateList = returnJson.getJSONArray("list");
        for (Object o : scheduleDateList) {
            JSONObject schedulDateObj  = (JSONObject) o;
            if (!mission.getMissionDate().equals(schedulDateObj.getString("date")))
                continue;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    confirmAppoint(schedulDateObj,req,user,mission);
                }
            }).start();

        }
    }


    private boolean isTimesToLogin(ZyyyUser user) {
        for (int i = 0; i <= 3; i++) {
            boolean isLogin = login.testSession(user.getSessionId());
            if (isLogin) return false;
            if (login.loginAndSave(user))
                return false;
        }
        LOG.info("3次登录失败");
        return true;
    }

    private void confirmAppoint(JSONObject schedulDateObj, JSONObject req, ZyyyUser user, UserMission mission) {
        JSONObject params = new JSONObject();
        JSONObject doctorSchedul = schedulDateObj.getJSONArray("doctor").getJSONObject(0);
        if (0 == doctorSchedul.getIntValue("count")){
            LOG.info("无号源");
            return;
        }
        JSONArray doctorSchedulList = doctorSchedul.getJSONArray("schedulList");
        for (Object o : doctorSchedulList) {
            JSONObject schedulList = (JSONObject) o;
            if (1 != schedulList.getIntValue("is_vary") ||  1 != schedulList.getIntValue("enable")
                    || 0 == schedulList.getIntValue("am_cont")) {
                LOG.info("无号源或停诊");
                continue;
            }
            String regId = schedulList.getString("reg_id");
            String regDate = schedulList.getString("date") + schedulList.getString("week_day")
                    + (schedulList.getIntValue("am_pm_flag") == 1 ? " 上午" : " 下午");

            params.put("reg_id", regId);
            params.put("date", regDate);
            params.put("phone", user.getLoginName());
            params.put("id_card", user.getIdCard());
            params.put("doct_name", doctorSchedul.getString("name"));
            params.put("dept_name", doctorSchedul.getString("dept_name"));
            params.put("user_name", user.getUserName());
            params.put("sex", user.getSex());
            params.put("card_no", mission.getCardNo());

            JSONArray noArr = schedulList.getJSONArray("no_arr");
            ZyyyCommon.sort(noArr,"reg_no",null);
            for (Object obj : noArr) {
                confirmThreadPool.execute(new ConfirmRunner(params, confirmReservation, obj, regId, req));
            }
        }
    }
}
class ConfirmRunner implements Runnable,Serializable{
    private static final long serialVersionUID = 1L;
    private final Logger LOG = Logger.getLogger(this.getClass());
    JSONObject params;
    ConfirmReservation confirmReservation;
    Object obj;
    String regId;
    JSONObject req;
    public ConfirmRunner(){
    }
    public ConfirmRunner(JSONObject params, ConfirmReservation confirmReservation
            , Object obj,String regId,JSONObject req) {
        super();
        this.params = params;
        this.confirmReservation = confirmReservation;
        this.obj = obj;
        this.regId = regId;
        this.req = req;
    }

    @Override
    public void run() {
        JSONObject regNoJson = (JSONObject) obj;
        Integer scheduleId = regNoJson.getInteger("schedule_id");
        String regNo = regNoJson.getString("reg_no");
        params.put("reg_id", regId);
        params.put("schedul_id", scheduleId);
        params.put("reg_no", regNo);
        req.put("params", params);
        ZyyyResponse response = confirmReservation.go(req);
        LOG.info(JSON.toJSONString(response)+",reg_no:"+regNo+"thread:"+Thread.currentThread().getName());
    }
}
//new Runnable() {
//@Override
//public void run() {
//        JSONObject params = new JSONObject();
//        JSONObject req = new JSONObject();
////                    ZyyyUser user = userRepository.findOne(mission.getUserId());
//
//        if (isTimesToLogin(user))
//        return;
//
//        params.put("doctor_name", mission.getDoctorName());
//        params.put("dept_name", mission.getDeptName());
//        params.put("yuanqu_type", mission.getHospitalId());
//        req.put("sessionId", user.getSessionId());
//        req.put("params", params);
//        ZyyyResponse response = doctorScheduleDetails.go(req);
//        if (!response.isConnectedSucc() || !response.isSuccessful()) {
//        LOG.info("获取排班失败");
//        return;
//        }
//        params.clear();
//        JSONObject returnJson = response.getReturnParams();
//        JSONArray scheduleDateList = returnJson.getJSONArray("list");
//        for (Object o : scheduleDateList) {
//        JSONObject schedulDateObj  = (JSONObject) o;
//        if (mission.getMissionDate().equals(schedulDateObj.getString("date")))
//        continue;
//        confirmAppoint(schedulDateObj,req,user,mission);
//        }
//        }
//        }