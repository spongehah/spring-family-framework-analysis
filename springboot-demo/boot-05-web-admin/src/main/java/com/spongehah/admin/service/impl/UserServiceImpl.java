package com.spongehah.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spongehah.admin.bean.User;
import com.spongehah.admin.mapper.UserMapper;
import com.spongehah.admin.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {
}
