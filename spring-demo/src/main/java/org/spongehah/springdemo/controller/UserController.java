package org.spongehah.springdemo.controller;

import org.junit.Test;
import org.spongehah.springdemo.beans.User;
import org.spongehah.springdemo.service.MessageService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class UserController {
    
    
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println("context 启动成功");
        MessageService messageService = applicationContext.getBean("messageService", MessageService.class);
        System.out.println(messageService.getMessage());
    }
    
    @Test
    public void test1() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        User myBean = applicationContext.getBean("myBean", User.class);
        System.out.println(myBean);
    }

    @Test
    public void test2() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        User user = applicationContext.getBean("user", User.class);
        user.add();
    }

}
