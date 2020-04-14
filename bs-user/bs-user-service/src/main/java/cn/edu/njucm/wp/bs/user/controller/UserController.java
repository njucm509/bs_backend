package cn.edu.njucm.wp.bs.user.controller;

import cn.edu.njucm.wp.bs.user.pojo.User;
import cn.edu.njucm.wp.bs.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    public ResponseEntity<User> login() {
        User user = null;
        return ResponseEntity.ok(user);
    }
}
