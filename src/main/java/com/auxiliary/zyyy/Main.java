package com.auxiliary.zyyy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.utils.DateUtil;
import com.auxiliary.zyyy.api.*;
import com.auxiliary.zyyy.mapper.ZyyyMapper;
import com.auxiliary.zyyy.model.UserMission;
import com.auxiliary.zyyy.model.ZyyyResponse;
import com.auxiliary.zyyy.model.ZyyyUser;
import com.auxiliary.zyyy.repository.UserMissionRepository;
import com.auxiliary.zyyy.repository.ZyyyUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    DoctorSearch doctorSearch;
    @Autowired
    RemainNum remainNum;

    @Autowired
    UserMissionRepository missionRepository;
    @Autowired
    ZyyyUserRepository userRepository;
    @Autowired
    ZyyyMapper zyyyMapper;

    public static final Integer POOL_SIZE = 5;

    ExecutorService missionThreadPool = Executors.newFixedThreadPool(POOL_SIZE);
    ExecutorService confirmThreadPool = Executors.newCachedThreadPool();

//    @Scheduled(cron = "*/2 58-59 23 * * ?")
//    @Scheduled(cron = "*/2 13-17 0 * * ?")
//    @Scheduled(cron = "*/2 58-59 14 * * ?")
//    @Scheduled(cron = "*/2 * 17-23 * * ?")
    @Scheduled(fixedDelay = 2000L)
    public void mainTask() {
        SimpleDateFormat sdf = new SimpleDateFormat(ZyyyConstant.DEFAULT_DATE_FORMAT);
        List<UserMission> list = missionRepository.findListByMissionDateAndIsDeleteAndMissionStatus(
                DateUtil.getDateByDays(5), "0", "0");
        list.addAll(missionRepository.findListByMissionDateAndIsDeleteAndMissionStatus(
                DateUtil.getDateByDays(6), "0", "0"));
        for (UserMission mission : list) {
            ZyyyUser user = userRepository.findOne(mission.getUserId());

            missionThreadPool.execute(new MissionRunner(missionRepository,
                    doctorScheduleDetails,doctorSearch,remainNum, confirmReservation, mission, login, user));
        }
    }

    /**
     * 开始主task之前更新user's session_id
     */
    @Scheduled(cron = "0 55 23 * * ?")
    public void testSession2UserTask() {
        List<ZyyyUser> readyMissionList = zyyyMapper.findMissionUserByDate(DateUtil.getDateByDays(8));
        for (ZyyyUser user : readyMissionList) {
            isTimesToLogin(user);
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
}

class MissionRunner implements Runnable, Serializable {
    private static final long serialVersionUID = 1L;

    private final Logger LOG = Logger.getLogger(this.getClass());

    private static ExecutorService confirmThreadPool = Executors.newCachedThreadPool();
    UserMissionRepository missionRepository;
    DoctorScheduleDetails doctorScheduleDetails;
    DoctorSearch doctorSearch;
    RemainNum remainNum;
    ConfirmReservation confirmReservation;
    UserMission mission;
    Login login;
    JSONObject req = new JSONObject();
    ZyyyUser user;

    private volatile boolean success = false;

    public MissionRunner() {
    }

    public MissionRunner(UserMissionRepository missionRepository, DoctorScheduleDetails doctorScheduleDetails,
                         DoctorSearch doctorSearch, RemainNum remainNum, ConfirmReservation confirmReservation,
                         UserMission mission, Login login, ZyyyUser user) {
        this.missionRepository = missionRepository;
        this.doctorScheduleDetails = doctorScheduleDetails;
        this.doctorSearch = doctorSearch;
        this.remainNum = remainNum;
        this.confirmReservation = confirmReservation;
        this.mission = mission;
        this.login = login;
        this.req = req;
        this.user = user;
    }
    //    @Override
//    public void run() {
//        JSONObject params = new JSONObject();
////        if (isTimesToLogin(user))
////            return;
//
//        params.put("doctor_name", mission.getDoctorName());
//        params.put("dept_name", mission.getDeptName());
//        params.put("yuanqu_type", mission.getHospitalId());
//        req.put("sessionId", user.getSessionId());
//        req.put("params", params);
//        ZyyyResponse response = doctorScheduleDetails.go(req);
//        if (!response.isConnectedSucc() || !response.isSuccessful()) {
//            LOG.info("获取排班失败");
//            return;
//        }
//        params.clear();
//        JSONObject returnJson = response.getReturnParams();
//        JSONArray scheduleDateList = returnJson.getJSONArray("list");
//        for (Object o : scheduleDateList) {
//            JSONObject schedulDateObj = (JSONObject) o;
//            if (!mission.getMissionDate().equals(schedulDateObj.getString("date")))
////            if (!"20170818".equals(schedulDateObj.getString("date")))
//                continue;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    confirmAppoint(schedulDateObj);
//                }
//            }).start();
//
//        }
//    }

    @Override
    public void run() {
        JSONObject params = new JSONObject();
//        if (isTimesToLogin(user))
//            return;

        params.put("keyword", mission.getDoctorName());
        params.put("hosId", mission.getHospitalId());
        req.put("sessionId", user.getSessionId());
        req.put("params", params);
        ZyyyResponse response = doctorSearch.go(req);
        if (!response.isConnectedSucc() || !response.isSuccessful()) {
            LOG.info("获取排班失败");
            return;
        }
        params.clear();
        JSONObject returnJson = response.getReturnParams();
        JSONArray scheduleDateList = returnJson.getJSONArray("list");
        for (Object o : scheduleDateList) {
            JSONObject schedulDateObj = (JSONObject) o;
            if (!mission.getMissionDate().equals(schedulDateObj.getString("date")))
//            if (!"20170818".equals(schedulDateObj.getString("date")))
                continue;
            new Thread(new Runnable() {
                @Override
                public void run() {
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
//            String regDate = schedulList.getString("date") + schedulList.getString("week_day")
//                    + (schedulList.getIntValue("am_pm_flag") == 1 ? " 上午" : " 下午");

            params.put("reg_id", regId);
            params.put("pre_date", schedulList.getString("pre_date").replaceAll("-",""));
            params.put("week_day",schedulList.getString("week_day"));
            params.put("yuanqu_type","1");
            params.put("type","1");
            params.put("phone", user.getLoginName());
            params.put("id_card", user.getIdCard());
            params.put("doct_name", doctorSchedul.getString("name"));
            params.put("doct_id","\"" + doctorSchedul.getString("id") + "\"");
            params.put("dept_id",doctorSchedul.getString("dept_id"));
            params.put("dept_name", doctorSchedul.getString("dept_name"));
            params.put("user_name", user.getUserName());
//            params.put("sex", user.getSex());
            params.put("card_no", "");
            params.put("fee",schedulList.getString("fee"));
            params.put("plan_id",schedulList.getLong("plan_id"));

            JSONObject remainParams = new JSONObject();
            remainParams.put("sourceId","12");
            remainParams.put("planId",schedulList.getLong("plan_id"));
            remainParams.put("ampmFlag",schedulList.getString("am_pm_flag"));
            remainParams.put("orderDate",schedulList.getString("pre_date"));

            JSONObject remainReq = new JSONObject();
            remainReq.put("sessionId",user.getSessionId());
            remainReq.put("params",remainParams);
            confirmThreadPool.execute(() -> {
                ZyyyResponse response = remainNum.go(remainReq);
                if (!response.isSuccessful())
                    return;
                response.getReturnParams().getJSONArray("list").forEach((json) -> {
                    JSONObject remain = (JSONObject) json;
                    JSONObject cReq = new JSONObject();
                    cReq.putAll(params);
                    cReq.put("clinic_time",remain.getString("timespan"));
                    cReq.put("pre_time_type",remain.getString("timeregion"));
                    cReq.put("reg_no",remain.getString("regno"));
                    confirmThreadPool.execute(() -> {
                        JSONObject confirmReq = new JSONObject();
                        confirmReq.put("sessionId",user.getSessionId());
                        confirmReq.put("params",cReq);
                        if (!success) {
                            success = confirmReservation.go(confirmReq).isSuccessful();
                            if (success) {
                                mission.setMissionStatus("1");
                                missionRepository.save(mission);
                            }
                        }
                    });
                });
            });

//            JSONArray noArr = schedulList.getJSONArray("no_arr");
//            ZyyyCommon.sort(noArr, "reg_no", null);
//            for (Object obj : noArr) {
//                confirmThreadPool.execute(new ConfirmRunner(
//                        params, confirmReservation, obj, regId, req, mission,missionRepository));
//            }
        }
    }
}

class ConfirmRunner implements Runnable, Serializable {
    private static final long serialVersionUID = 1L;
    private final Logger LOG = Logger.getLogger(this.getClass());
    JSONObject params;
    ConfirmReservation confirmReservation;
    Object obj;
    String regId;
    JSONObject req;
    UserMission mission;
    UserMissionRepository missionRepository;

    public ConfirmRunner() {
    }

    public ConfirmRunner(JSONObject params, ConfirmReservation confirmReservation
            , Object obj, String regId, JSONObject req, UserMission mission, UserMissionRepository missionRepository) {
        super();
        this.params = params;
        this.confirmReservation = confirmReservation;
        this.obj = obj;
        this.regId = regId;
        this.req = req;
        this.mission = mission;
        this.missionRepository = missionRepository;
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
        if (!response.isConnectedSucc() || !response.isSuccessful()) {
            LOG.info("预约失败");
            return;
        }
        mission.setMissionStatus("1");
        missionRepository.save(mission);
        LOG.info("预约成功：" + mission.getCardNo() + ":" + mission.getDoctorName() + "预约序号：" + regId);
//        LOG.info(JSON.toJSONString(response) + ",reg_no:" + regNo + "thread:" + Thread.currentThread().getName());
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