package org.spongehah.springdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserAspect {
    
    @Pointcut("execution(* org.spongehah.springdemo.beans.*.add(..))")
    private void p1(){}

    @Pointcut("execution(* org.spongehah.springdemo.*.*.getMessage(..))")
    private void p2(){}

    @Before(value = "p1()")
    public void before1(){
        System.out.println("before");
    }
    
    @Before(value = "p2()")
    public void before2(){
        System.out.println("before");
    }
    
}
