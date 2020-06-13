package cn.edu.njucm.wp.bs.user.controller;

import cn.edu.njucm.wp.bs.common.encrypt.MD5Util;
import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.user.pojo.User;
import cn.edu.njucm.wp.bs.user.service.UserService;
import cn.edu.njucm.wp.bs.user.vo.RegisterForm;
import cn.edu.njucm.wp.bs.user.vo.UserVO;
import cn.edu.njucm.wp.bs.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("login")
    public ResponseEntity<HashMap<String, Object>> login(@RequestBody User user) {
        log.info("user: {}", user);
        HashMap<String, Object> res = new HashMap<>();
        User u = userService.queryUser(user.getName(), user.getPassword());
        log.info("find user: {}", u);
        if (u == null) {
            res.put("code", 0);
            res.put("msg", "用户不存在");
        } else if (!MD5Util.encrypt(user.getPassword()).equals(u.getPassword())) {
            res.put("code", 0);
            res.put("msg", "密码错误");
        } else if (u.getFlag() == 0) {
            res.put("code", 0);
            res.put("msg", "请等待管理员审核");
        } else {
            res.put("code", 1);
            res.put("msg", "登陆成功");
            res.put("user", u);
        }
        return ResponseEntity.ok(res);
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
    public ResponseEntity<UserVO> create(@RequestBody UserVO userVO, HttpServletRequest request) {
        log.info("register: {}", userVO);
        if (userService.getByName(userVO.getName()) != null) {
            return ResponseEntity.ok(null);
        }
        User user = new User();
        UserVO res = new UserVO();
        BeanUtils.copyProperties(userVO, user);
        if (IPUtil.getIp(request) != 0) {
            user.setIp(IPUtil.getIp(request));
        }
        boolean flag = userService.create(user) == 1;
        if (flag) {
            User cur = userService.getByName(userVO.getName());
            Boolean f = userService.bindRole(cur.getId(), userVO.getRoleId());
            List<Integer> roleId = userService.getRoleIdByUserId(cur.getId());
            BeanUtils.copyProperties(cur, res);
            res.setRoleId(roleId);
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("update")
    public ResponseEntity<Boolean> update(@RequestBody UserVO userVO) {
        log.info("user update: {}", userVO);
        log.info("user vo: {}", userVO);
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        Integer res = userService.update(user);
        Integer i = userService.deleteUserRole(user.getId());
        Boolean flag = userService.bindRole(user.getId(), userVO.getRoleId());
        if (res == 1) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    @RequestMapping("role/{id}")
    public ResponseEntity<List<Integer>> getRoleIdByUserId(@PathVariable("id") Long id) {
        log.info("id: {}", id);
        List<Integer> list = userService.getRoleIdByUserId(id);
        if (CollectionUtils.isEmpty(list)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("page")
    public ResponseEntity<PageResult<UserVO>> getUserByPage(PageParam param) {
        PageResult<UserVO> result = userService.getUserByPageAndSort(param);
        if (result == null | result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("flag/check")
    public ResponseEntity<Boolean> checkUserFlag(@RequestBody User user) {
        log.info("flag check user: {}", user);
        return ResponseEntity.ok(userService.checkFlag(user));
    }

    @GetMapping("mb/down")
    public ResponseEntity<byte[]> down() {
        log.info("down user.csv");
        String colNames = "姓名,手机号,邮箱";
        String data = "张三,13012343456,tmp@tmp.com";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "user.csv");
        ResponseEntity<byte[]> res = null;
        try {
            File tempFile = File.createTempFile("user", "csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
            log.info("col : {}", colNames);
            bw.write(colNames);
            bw.newLine();
            log.info("data : {}", data);
            bw.write(data);
            bw.flush();
            bw.close();
            res = new ResponseEntity<>(FileUtils.readFileToByteArray(tempFile), headers, HttpStatus.CREATED);
            boolean b = tempFile.delete();
            log.info("temp file delete {} ...", b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @RequestMapping("/multi")
    public ResponseEntity<Void> multi(MultipartFile file) {
        log.info("user: {}", file.getOriginalFilename());
        try {
            userService.multi(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("detail/{id}")
    public ResponseEntity<UserVO> getUserById(@PathVariable("id") Long id) {
        UserVO userVO = userService.getUserById(id);
        if (userVO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userVO);
    }

    @GetMapping("list")
    public ResponseEntity<List<UserVO>> getUserList() {
        List<UserVO> list = userService.getAll();

        if (CollectionUtils.isEmpty(list)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(list);
    }
}
