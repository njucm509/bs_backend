package cn.edu.njucm.wp.bs.auth.vo;

import cn.edu.njucm.wp.bs.auth.pojo.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO extends Role {
    private List<Integer> permissionId;

    private List<Integer> fieldId;
}
