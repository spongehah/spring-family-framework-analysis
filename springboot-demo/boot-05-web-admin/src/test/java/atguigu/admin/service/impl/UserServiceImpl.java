package atguigu.admin.service.impl;

import atguigu.admin.bean.User;
import atguigu.admin.mapper.UserMapper;
import atguigu.admin.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}
