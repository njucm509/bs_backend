package cn.edu.njucm.wp.bs.user.service;

import cn.edu.njucm.wp.bs.user.pojo.User;

import java.util.List;

public interface UserService {
    User queryUser(String username, String password);

    Integer create(User user);

    User getByName(String name);

    Boolean bindRole(Long id, List<Integer> roleId);

    List<Integer> getRoleIdByUserId(Long id);
}
