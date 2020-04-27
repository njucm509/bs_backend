package cn.edu.njucm.wp.bs.auth.service.impl;

import cn.edu.njucm.wp.bs.auth.bo.UserInfo;
import cn.edu.njucm.wp.bs.auth.client.UserClient;
import cn.edu.njucm.wp.bs.auth.mapper.PermissionMapper;
import cn.edu.njucm.wp.bs.auth.mapper.RoleMapper;
import cn.edu.njucm.wp.bs.auth.pojo.Permission;
import cn.edu.njucm.wp.bs.auth.pojo.Role;
import cn.edu.njucm.wp.bs.auth.properties.JwtProperties;
import cn.edu.njucm.wp.bs.auth.service.AuthService;
import cn.edu.njucm.wp.bs.auth.utils.JwtUtils;
import cn.edu.njucm.wp.bs.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    PermissionMapper permissionMapper;

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

        return roleMapper.insert(role);
    }

    @Override
    public Integer delete(Integer id) {
        Role role = new Role();
        role.setId(id);
        return roleMapper.delete(role);
    }

    @Override
    public List<Role> list() {
        return roleMapper.selectAll();
    }

    @Override
    public Integer update(Role role) {
        role.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));

        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public Boolean check(Role role) {
        return roleMapper.check(role) == 1 ? true : false;
    }

    @Override
    public List<Integer> getRoleIdByUserId(Long id) {
        return roleMapper.getRoleIdByUserId(id);
    }

    @Override
    public Role getRoleByUserId(Long id) {
        roleMapper.getRoleByUserId(id);
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
        return null;
    }

    @Override
    public Integer bindRole(Long userId, List<Integer> roleId) {
        Integer res = 0;
        for (Integer id : roleId) {
            res += roleMapper.bindRole(userId, id);
        }
        return res;
    }

    @Override
    public List<Permission> permissionList() {
        return permissionMapper.selectAll();
    }

}
