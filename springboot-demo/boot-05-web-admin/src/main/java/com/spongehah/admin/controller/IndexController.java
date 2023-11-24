package com.spongehah.admin.controller;

import com.spongehah.admin.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class IndexController {

    @GetMapping(value = {"/", "/login"})
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String main(User user, HttpSession session, Model model) { // RedirectAttributes

        if (StringUtils.hasLength(user.getUserName()) && "123".equals(user.getPassword())) {
            // 把登陆成功的用户保存起来
            session.setAttribute("loginUser", user);
            // 登录成功重定向到main.html;  重定向防止表单重复提交
            return "redirect:/main.html";
        } else {
            model.addAttribute("msg", "账号密码错误");
            // 回到登录页面
            return "login";
        }
    }

    @GetMapping("/main.html")
    public String mainPage(HttpSession session, Model model) {
        log.info("当前方法是：{}", "mainPage");
        return "main";
    }
}
