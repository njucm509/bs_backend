package cn.edu.njucm.wp.bs.auth.controller;

import cn.edu.njucm.wp.bs.auth.pojo.Role;
import cn.edu.njucm.wp.bs.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("role")
public class AuthController {

    @Autowired
    AuthService authService;

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

}
