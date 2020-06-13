package cn.edu.njucm.wp.bs.user.api;

import cn.edu.njucm.wp.bs.user.pojo.User;
import cn.edu.njucm.wp.bs.user.vo.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {
    @GetMapping("query")
    public User queryUser(@RequestParam("username") String username, @RequestParam("password") String password);

    @GetMapping("detail/{id}")
    public UserVO getUserById(@PathVariable("id") Long id);
}
