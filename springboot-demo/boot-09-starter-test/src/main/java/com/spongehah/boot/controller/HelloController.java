package com.spongehah.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;

@RestController
public class HelloController {
    
    @Autowired
    private ThreadPoolExecutor myThreadPool;
    
    @GetMapping("/hello")
    public String hello() {
        return myThreadPool.getCorePoolSize() + ":" + myThreadPool.getMaximumPoolSize();
    }
}
