package com.auxiliary;

import com.auxiliary.model.MailMission;
import com.auxiliary.repository.MailMissionRepository;
import com.auxiliary.service.MailService;
import com.auxiliary.utils.MailPropertiesUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/7/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuxiliaryApplication.class)
public class SendMailTest {
    @Autowired
    private MailService mailService;
    @Value("${spring.mail.username}")
    private String mailTo;

    @Autowired
    MailPropertiesUtil mailPropertiesUtil;
//    @Value("${mail.properties.path}")
//    private String propsPath;
    @Autowired
    private MailMissionRepository missionRepository;
    @Test
    public void sendMail() throws MessagingException {
        mailService.sendSimpleMail(mailTo,"Test","this is the test for sending mail");
    }
    @Test
    public void sendHtmlMailTest(){
        StringBuffer sb = new StringBuffer();
        sb.append("<h1>HTML</h1>")
                .append("<p style='color:#F00'>红色字</p>")
                .append("<p style='text-align:right'>右对齐</p>");
        mailService.sendHtmlMail(mailTo,"HTML TEST",sb.toString());
    }
    @Test
    public void testProp() throws Exception {
//        System.out.println(MailPropertiesUtil.class.newInstance().getProperty("mail.task.cron"));
//        System.out.println(MailPropertiesUtil.class.newInstance().getProperty("mail.task.cron","dd"));
        System.out.println(mailPropertiesUtil.setProperty("mail.task.cron","*/10 * * * * ?"));
    }

    @Test
    public void missionTest(){
        MailMission mission = new MailMission();
        mission.setSubject("Test");
        mission.setContent("This is the test for sending mail.");
        mission.setCreateDate(new Date());
        mission.setIsDelete('0');
        missionRepository.save(mission);
        List<MailMission> missions = missionRepository.findAllByIsDelete('0');
//        System.out.println(missions.toString());
    }
}
