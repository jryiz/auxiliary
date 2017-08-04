package com.auxiliary.task;

import com.auxiliary.model.MailFlow;
import com.auxiliary.model.MailMission;
import com.auxiliary.repository.MailMissionRepository;
import com.auxiliary.service.MailService;
import com.auxiliary.utils.DateUtil;
import com.auxiliary.utils.MailPropertiesUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @auther ucmed Wenjun Choi
 * @create 2017/7/17
 */
@Component
public class MailTask {
    private final Logger LOG = Logger.getLogger(this.getClass());
    @Autowired
    private MailPropertiesUtil mailPropertiesUtil;
    @Autowired
    private MailService mailService;
    @Autowired
    private MailMissionRepository missionRepository;

    public void taskTest(){
        mailPropertiesUtil.getProperty("mail.task.cron");
        System.out.println("task task task");
    }

    @Scheduled(cron="${mail.task.cron}")
    public void execute(){
        LOG.info("send mail....");
        List<MailMission> missions;
        try {
            missions = missionRepository.findAllByIsDelete('0');
            for ( MailMission mission:missions) {
                if (isTimesUp(mission)) {
                    mailService.sendSimpleMail(mission.getOwner(),
                            mission.getSubject(), mission.getContent());
                    int times = mission.getOperationTimes();
                    mission.setOperationTimes(++times);
                    mission.setUpdateDate(new Date());
                    missionRepository.save(mission);
//                Thread.sleep(5000);
                }
            }
        } catch (MessagingException e){
            LOG.error("send mail failed",e);
        } catch (Exception e) {
            LOG.error("send mail failed",e);
        }
    }

    private boolean isTimesUp(MailMission mission){
        if(mailPropertiesUtil == null)
            mailPropertiesUtil = new MailPropertiesUtil();
        Integer intervalDay = Integer.valueOf(mailPropertiesUtil.getProperty("mail.next.time"));
        Date lastMissionDate = mission.getUpdateDate();
//        System.out.println(DateUtil.getyyyyMMdd(lastMissionDate));
        if(null == lastMissionDate)
            lastMissionDate = new Date();
        Date nextMissionDate = null;
        Date nextDate = DateUtil.dayAdd(lastMissionDate,intervalDay);
        if(null == mission.getDayOfWeek() || -1 == mission.getDayOfWeek()){
            nextMissionDate = nextDate;
            return DateUtil.getyyyyMMdd(new Date()).equals(DateUtil.getyyyyMMdd(nextMissionDate));
        }
//        System.out.println(DateUtil.getyyyyMMdd(nextDate));
        Calendar c = Calendar.getInstance();
        c.setTime(nextDate);
        int nextMissionWeekId = c.get(Calendar.DAY_OF_WEEK) ;//DateUtil.getWeekId(DateUtil.getyyyyMMdd(nextDate));
        nextMissionDate = DateUtil.dayAdd(nextDate,
                nextMissionWeekId*2 - mission.getDayOfWeek()*2 + Calendar.DAY_OF_WEEK > 0 ?
                mission.getDayOfWeek() - nextMissionWeekId  : nextMissionWeekId - mission.getDayOfWeek() + 7);
        if (DateUtil.getyyyyMMdd(nextMissionDate).compareTo(DateUtil.getyyyyMMdd(new Date())) < 0) {
            mission.setUpdateDate(nextMissionDate);
            missionRepository.save(mission);
        }
        return DateUtil.getyyyyMMdd(new Date()).equals(DateUtil.getyyyyMMdd(nextMissionDate));


    }

    public static void main(String[] args) throws Exception {
        MailTask t = new MailTask();
        MailMission m = new MailMission();
        m.setUpdateDate( DateUtil.dayAdd(new Date(),1));
        System.out.println(t.isTimesUp(m));
        m.setUpdateDate(new Date());
        System.out.println(t.isTimesUp(m));
    }
}
