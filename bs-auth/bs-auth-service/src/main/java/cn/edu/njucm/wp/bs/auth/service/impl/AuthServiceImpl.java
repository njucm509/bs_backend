package cn.edu.njucm.wp.bs.auth.service.impl;

import cn.edu.njucm.wp.bs.auth.bo.UserInfo;
import cn.edu.njucm.wp.bs.auth.client.UserClient;
import cn.edu.njucm.wp.bs.auth.mapper.AuthMapper;
import cn.edu.njucm.wp.bs.auth.pojo.Role;
import cn.edu.njucm.wp.bs.auth.properties.JwtProperties;
import cn.edu.njucm.wp.bs.auth.service.AuthService;
import cn.edu.njucm.wp.bs.auth.utils.JwtUtils;
import cn.edu.njucm.wp.bs.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthMapper authMapper;

    @Autowired
    UserClient userClient;

    @Autowired
    JwtProperties properties;

    @Override
    public Integer create(Role role) {
        if (role.getCreatedAt() == null) {
            role.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }

        if (role.getUpdatedAt() == null) {
            role.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }

        return authMapper.insert(role);
    }

    @Override
    public Integer delete(Integer id) {
        Role role = new Role();
        role.setId(id);
        return authMapper.delete(role);
    }

    @Override
    public List<Role> list() {
        return authMapper.selectAll();
    }

    @Override
    public Integer update(Role role) {
        role.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));

        return authMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public Boolean check(Role role) {
        return authMapper.check(role) == 1 ? true : false;
    }

    @Override
    public Integer getRoleIdByUserId(Long id) {
        authMapper.getRoleIdByUserId(id);
        return null;
    }

    @Override
    public Role getRoleByUserId(Long id) {
        authMapper.getRoleByUserId(id);
        return null;
    }

    @Override
    public String authentication(String username, String password) {
        try {
            User user = userClient.queryUser(username, password);
            if (user == null) {
                return null;
            }
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getName()), properties.getPrivateKey(), properties.getExpire());
            log.info("token generate: {}", token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;    }
}
