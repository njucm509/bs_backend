package cn.edu.njucm.wp.bs.user.vo;

import cn.edu.njucm.wp.bs.user.pojo.User;
import lombok.Data;

import java.util.List;

@Data
public class UserVO extends User {
    private List<Integer> roleId;
}
