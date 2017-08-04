package com.auxiliary.controller;

import com.auxiliary.model.MailMission;
import com.auxiliary.repository.MailMissionRepository;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/7/18
 */
@Controller
@RequestMapping(value = "/mission")
public class MailMissionController {
    private static final Logger LOG = Logger.getLogger(MailMissionController.class);
    @Autowired
    private MailMissionRepository missionRepository;

    @GetMapping("/all")
    public String getMissionList(Model model){
        List<MailMission> list = missionRepository.findAllByIsDelete('0');
        model.addAttribute("list",list);
        return "mission";
    }

    @PostMapping("/all")
    public String addMission(MailMission mission){
        mission.setIsDelete('0');
        mission.setCreateDate(new Date());
        MailMission m = missionRepository.save(mission);
        System.out.println(m.toString());
        return "redirect:all";
    }
}
