package cn.edu.njucm.wp.bs.user.service.impl;

import cn.edu.njucm.wp.bs.common.encrypt.MD5Util;
import cn.edu.njucm.wp.bs.user.mapper.UserMapper;
import cn.edu.njucm.wp.bs.user.pojo.User;
import cn.edu.njucm.wp.bs.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User queryUser(String username, String password) {
        User record = new User();
        record.setName(username);
        User user = userMapper.selectOne(record);
        if (user == null) {
            return null;
        }
        if (!user.getPassword().equals(MD5Util.encrypt(password))) {
            return null;
        }
        return user;
    }
}
