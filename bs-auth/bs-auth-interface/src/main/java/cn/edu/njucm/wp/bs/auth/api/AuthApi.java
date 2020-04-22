package cn.edu.njucm.wp.bs.auth.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("role")
public interface AuthApi {

    @PostMapping("bind")
    Boolean bindRole(@RequestParam("userId") Long userId, @RequestParam("roleId") List<Integer> roleId);

    @PostMapping("user")
    List<Integer> getRoleByUserId(@RequestParam("id") Long id);
}
