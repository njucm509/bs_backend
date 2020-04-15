package cn.edu.njucm.wp.bs.auth.controller;

import cn.edu.njucm.wp.bs.auth.pojo.Role;
import cn.edu.njucm.wp.bs.auth.bo.UserRole;
import cn.edu.njucm.wp.bs.auth.properties.JwtProperties;
import cn.edu.njucm.wp.bs.auth.service.AuthService;
import cn.edu.njucm.wp.bs.util.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("role")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtProperties properties;

    @PostMapping("create")
    public ResponseEntity<Boolean> create(@RequestBody Role role) {
        Boolean flag = authService.check(role);
        if (flag) {
            return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        }
        Integer res = authService.create(role);
        if (res == 0) {
            return (ResponseEntity<Boolean>) ResponseEntity.notFound();
        }
        return ResponseEntity.ok(res == 1);
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(authService.delete(id) == 1);
    }

    @PostMapping("update")
    public ResponseEntity<Boolean> update(@RequestBody Role role) {
        log.info("role: {}", role);
        Boolean flag = authService.check(role);
        if (flag) {
            return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(authService.update(role) == 1);
    }

    @GetMapping("list")
    public ResponseEntity<List<Role>> list() {
        List<Role> res = authService.list();
        if (CollectionUtils.isEmpty(res)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("authorize")
    public ResponseEntity<Boolean> addRole2User(UserRole userRole) {
        Role role = authService.getRoleByUserId(userRole.getAdminId());

        return ResponseEntity.ok(true);
    }

    @PostMapping("accredit")
    public ResponseEntity<Void> authentication(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
        String token = authService.authentication(username, password);
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        log.info("prop: {}", properties);
        CookieUtils.setCookie(request, response, properties.getCookieName(), token, properties.getCookieMaxAge(), false);
        Cookie[] cookies = request.getCookies();
        return ResponseEntity.ok().build();
    }
}
