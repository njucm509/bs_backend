package cn.edu.njucm.wp.bs.auth.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("role")
public interface AuthApi {

    @PostMapping("bind")
    Boolean bindRole(Long userId, List<Integer> roleId);

    @PostMapping("user")
    List<Integer> getRoleByUserId(Long id);
}
