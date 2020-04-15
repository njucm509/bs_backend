package cn.edu.njucm.wp.bs.auth.bo;

import lombok.Data;

import java.util.List;

@Data
public class UserRole {

    private Long adminId;

    private Long userId;

    private List<Integer> roles;
}
