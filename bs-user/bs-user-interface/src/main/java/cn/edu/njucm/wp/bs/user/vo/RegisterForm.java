package cn.edu.njucm.wp.bs.user.vo;

import cn.edu.njucm.wp.bs.user.pojo.User;
import lombok.Data;

@Data
public class RegisterForm extends User {
    private Integer roleId;
}
