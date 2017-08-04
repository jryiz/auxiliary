package com.auxiliary;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/7/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuxiliaryApplication.class)
@Component
public class ScheduledTest {
    private final Logger LOG = Logger.getLogger(this.getClass());

    @Test
    @Scheduled(cron = "0/20 * * * * ?")
    public void taskTest(){
        System.out.println("task task task");
    }
}
