package org.spongehah.springdemo.service.impl;


import org.spongehah.springdemo.beans.User;
import org.spongehah.springdemo.dao.MessageDao;
import org.spongehah.springdemo.service.MessageService;

public class MessageServiceImpl implements MessageService {
    
    private MessageDao messageDao;
    
    private User user;

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return "messageServiceImpl......" + user.toString() + messageDao.getMessage();
    }
}