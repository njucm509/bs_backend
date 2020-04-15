package cn.edu.njucm.wp.bs.auth.service;

import cn.edu.njucm.wp.bs.auth.pojo.Role;

import java.util.List;

public interface AuthService {
    Integer create(Role role);

    Integer delete(Integer id);

    List<Role> list();

    Integer update(Role role);

    Boolean check(Role role);
}