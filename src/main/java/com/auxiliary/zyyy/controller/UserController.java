package com.auxiliary.zyyy.controller;

import com.auxiliary.zyyy.Main;
import com.auxiliary.zyyy.api.Login;
import com.auxiliary.zyyy.model.ZyyyUser;
import com.auxiliary.zyyy.repository.ZyyyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/9
 */
@Controller
@RequestMapping(value = "/zyyy/user")
public class UserController {
    @Autowired
    ZyyyUserRepository userRepository;
    @Autowired
    Login login;
    @Autowired
    Main main;
    @GetMapping("/all")
    public String getUserList(Model model){
        List<ZyyyUser> list = userRepository.findAllByIsDelete("0");
        model.addAttribute("list",list);
        return "user";
    }
    @PostMapping("/all")
    public String addMission(ZyyyUser user){
        login.loginAndSave(user);
        return "redirect:all";
    }

    @GetMapping("/go")
    @ResponseBody
    public String testMain(Model model){
        main.mainTask();
        return "test main";
    }
}
