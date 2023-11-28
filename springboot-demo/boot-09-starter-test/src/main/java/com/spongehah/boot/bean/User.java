package com.spongehah.boot.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
public class User {
    
    @Id
    private Long id;
    private String userName;
    @Size(min = 0, max = 130)
    private Integer age;
    @Email
    private String email;
}
