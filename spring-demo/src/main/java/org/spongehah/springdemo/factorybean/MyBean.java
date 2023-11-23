package org.spongehah.springdemo.factorybean;

import org.spongehah.springdemo.beans.User;
import org.springframework.beans.factory.FactoryBean;

public class MyBean implements FactoryBean<User> {

    @Override
    public User getObject() throws Exception {
        return new User("zs",18,"zs@163.com");
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
