package com.spongehah.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RequestAttributeController {
    @GetMapping("/goto")
    public String gotoPage(HttpServletRequest request,
                           Model model) {
        model.addAttribute("hello", "hello world");
        request.setAttribute("msg", "成功了。。。");
        return "forward:/success";
    }
    
    @ResponseBody
    @GetMapping("/success")
    public Map success(@RequestAttribute("msg") String msg,
                       HttpServletRequest request) {
        Object msg1 = request.getAttribute("msg");
        Object hello = request.getAttribute("hello");
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        map.put("msg1", msg1);
        map.put("hello", hello);
        return map;
    }
}
