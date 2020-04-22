package cn.edu.njucm.wp.bs.user.controller;

import cn.edu.njucm.wp.bs.user.pojo.User;
import cn.edu.njucm.wp.bs.user.service.UserService;
import cn.edu.njucm.wp.bs.user.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    @PostMapping("check")
    public ResponseEntity<String> checkName(String name) {
        User user = userService.getByName(name);
        if (user != null) {
            return ResponseEntity.ok("用户名已存在");
        } else {
            return ResponseEntity.ok("ok");
        }
    }

    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userService.queryUser(username, password);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("create")
    public ResponseEntity<UserVO> create(@RequestBody UserVO userVO) {
        if (userService.getByName(userVO.getName()) != null) {
            return ResponseEntity.ok(null);
        }
        User user = new User();
        UserVO res = new UserVO();
        BeanUtils.copyProperties(userVO, user);
        boolean flag = userService.create(user) == 1;
        if (flag) {
            User cur = userService.getByName(userVO.getName());
            Boolean f = userService.bindRole(cur.getId(), userVO.getRoleId());
            List<Integer> roleId = userService.getRoleIdByUserId(cur.getId());
            BeanUtils.copyProperties(cur, res);
            res.setRoleId(userVO.getRoleId());
        }
        return ResponseEntity.ok(res);
    }

    public ResponseEntity<UserVO> update(@RequestBody UserVO userVO) {
        log.info("user vo: {}", userVO);
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        return null;
    }
}
