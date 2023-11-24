package com.spongehah.boot.controller;

import com.spongehah.boot.pojo.Person;
import com.spongehah.boot.pojo.Pet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {
    
    @PostMapping("/savePerson") 
    public Person savePerson(@RequestBody Person person) {
        return person;
    }
    
    @GetMapping("/getPerson") 
    public Person getPerson() {
        return new Person("小张",18,new Pet("小黑","2"));
    }
}
