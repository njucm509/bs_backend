package cn.edu.njucm.wp.bs.user.service.impl;

import cn.edu.njucm.wp.bs.common.encrypt.MD5Util;
import cn.edu.njucm.wp.bs.user.client.AuthClient;
import cn.edu.njucm.wp.bs.user.mapper.UserMapper;
import cn.edu.njucm.wp.bs.user.pojo.User;
import cn.edu.njucm.wp.bs.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    AuthClient authClient;

    @Override
    public User queryUser(String username, String password) {
        User record = new User();
        record.setName(username);
        return userMapper.selectOne(record);
    }

    @Override
    public Integer create(User user) {
        if (user.getPassword() != null) {
            user.setPassword(MD5Util.encrypt(user.getPassword()));
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }
        if (user.getUpdatedAt() == null) {
            user.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }
        return userMapper.insertSelective(user);
    }

    @Override
    public User getByName(String name) {
        User record = new User();
        record.setName(name);
        return userMapper.selectOne(record);
    }

    @Override
    public Boolean bindRole(Long userId, List<Integer> roleId) {
        return authClient.bindRole(userId, roleId);
    }

    @Override
    public List<Integer> getRoleIdByUserId(Long id) {
        return authClient.getRoleByUserId(id);
    }

    @Override
    public Integer update(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }
}
