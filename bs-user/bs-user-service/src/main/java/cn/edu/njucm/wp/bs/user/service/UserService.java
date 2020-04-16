package cn.edu.njucm.wp.bs.user.service;

import cn.edu.njucm.wp.bs.user.pojo.User;

public interface UserService {
    User queryUser(String username, String password);

    Integer register(User user);
}
