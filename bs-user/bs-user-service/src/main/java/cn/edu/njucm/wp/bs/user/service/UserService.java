package cn.edu.njucm.wp.bs.user.service;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.user.pojo.User;
import cn.edu.njucm.wp.bs.user.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User queryUser(String username, String password);

    Integer create(User user);

    User getByName(String name);

    Boolean bindRole(Long id, List<Integer> roleId);

    List<Integer> getRoleIdByUserId(Long id);

    Integer update(User user);

    PageResult<UserVO> getUserByPageAndSort(PageParam param);

    Boolean checkFlag(User user);

    void multi(MultipartFile file) throws IOException;

    UserVO getUserById(Long id);

    List<UserVO> getAll();

    Integer deleteUserRole(Long id);
}
