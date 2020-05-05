package cn.edu.njucm.wp.bs.auth.controller;

import cn.edu.njucm.wp.bs.auth.pojo.Permission;
import cn.edu.njucm.wp.bs.auth.pojo.Role;
import cn.edu.njucm.wp.bs.auth.bo.UserRole;
import cn.edu.njucm.wp.bs.auth.properties.JwtProperties;
import cn.edu.njucm.wp.bs.auth.service.AuthService;
import cn.edu.njucm.wp.bs.auth.vo.RoleVO;
import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
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
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtProperties properties;

    @PostMapping("role/create")
    public ResponseEntity<Boolean> create(@RequestBody RoleVO role) {
        Boolean flag = authService.check(role);
        if (flag) {
            return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        }
        Integer res = authService.create(role);
        if (res == 0) {
            return (ResponseEntity<Boolean>) ResponseEntity.notFound();
        }
        if (role.getPermissionId() != null && !CollectionUtils.isEmpty(role.getPermissionId())) {
            authService.bindPermission(role.getId(), role.getPermissionId());
        }
        if (role.getFieldId() != null && !CollectionUtils.isEmpty(role.getFieldId())) {
            authService.bindField(role.getId(), role.getFieldId());
        }
        return ResponseEntity.ok(res == 1);
    }

    @PostMapping("role/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(authService.delete(id) == 1);
    }

    @PostMapping("role/update")
    public ResponseEntity<Boolean> update(@RequestBody RoleVO role) {
        log.info("role: {}", role);
        Boolean flag = authService.check(role);
        if (flag) {
            return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(authService.update(role) == 1);
    }

    @GetMapping("role/page")
    public ResponseEntity<PageResult<RoleVO>> list(PageParam param) {
        PageResult<RoleVO> result = authService.list(param);
        if (result == null | result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("role/list")
    public ResponseEntity<List<Role>> list() {
        List<Role> list = authService.list();
        if (CollectionUtils.isEmpty(list)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("permission/list")
    public ResponseEntity<List<Permission>> permissionList() {
        List<Permission> res = authService.permissionList();
        if (CollectionUtils.isEmpty(res)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("permission/create")
    public ResponseEntity<Boolean> permissionCreate(@RequestBody Permission permission) {
        Boolean flag = authService.check(permission);
        if (flag) {
            return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        }
        Boolean res = authService.create(permission);
        if (!res) {
            return (ResponseEntity<Boolean>) ResponseEntity.notFound();
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("permission/update")
    public ResponseEntity<Boolean> permissionUpdate(@RequestBody Permission permission) {
        Boolean flag = authService.check(permission);
        if (flag) {
            return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        }
        Boolean res = authService.update(permission);
        if (!res) {
            return (ResponseEntity<Boolean>) ResponseEntity.notFound();
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("role/authorize")
    public ResponseEntity<Boolean> addRole2User(UserRole userRole) {
        Role role = authService.getRoleByUserId(userRole.getAdminId());

        return ResponseEntity.ok(true);
    }

    @PostMapping("role/accredit")
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

    @PostMapping("role/bind")
    public ResponseEntity<Boolean> bindRole(Long userId, @RequestParam("roleId") List<Integer> roleId) {
        return ResponseEntity.ok(authService.bindRole(userId, roleId) >= 1 ? true : false);
    }

    @PostMapping("role/user")
    public ResponseEntity<List<Integer>> getRoleByUserId(@RequestParam("id") Long id) {
        List<Integer> roleId = authService.getRoleIdByUserId(id);

        return ResponseEntity.ok(roleId);
    }

    @GetMapping("role/permissionId")
    public ResponseEntity<List<Integer>> getPermissionIdByRoleId(@RequestParam("ids") List<Integer> ids) {
        List<Integer> list = authService.getPermissionIdByRoleId(ids);
        if (CollectionUtils.isEmpty(list)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

}
