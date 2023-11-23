package com.spongehah.boot.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    
    @GetMapping("/hello")
    public Map<String, Object> hello(@RequestParam("id") Integer id, 
                                     @RequestParam Map<String, String> map) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", id);
        resultMap.put("map", map);
        return resultMap; 
    }

    @GetMapping("/hello/{id}/{age}")
    public Map<String, Object> hello2(@PathVariable("id") Integer id,
                                      @PathVariable Map<String, String> map) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", id);
        resultMap.put("map", map);
        return resultMap;
    }

    @GetMapping("/hello3")
    public Map<String, Object> hello3(@RequestHeader("User-Agent") String userAgent,
                                      @RequestHeader Map<String, String> map) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userAgent", userAgent);
        resultMap.put("map", map);
        return resultMap;
    }

    @GetMapping("/hello4")
    public Map<String, Object> hello4(@CookieValue("b-user-id") String buserid,
                                      @CookieValue("b-user-id") Cookie cookie) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("b-user-id", buserid);
        resultMap.put("cookie", cookie.getName() + cookie.getValue());
        return resultMap;
    }

    @PostMapping("/hello5")
    public Map<String, Object> hello5(@RequestBody String content) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("content", content);
        return resultMap;
    }
}
