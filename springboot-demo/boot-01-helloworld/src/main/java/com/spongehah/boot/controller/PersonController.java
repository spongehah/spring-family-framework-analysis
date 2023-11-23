package com.spongehah.boot.controller;

import com.spongehah.boot.pojo.Person;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {
    
    @PostMapping("/savePerson") 
    public Person savePerson(@RequestBody Person person) {
        return person;
    }
}
