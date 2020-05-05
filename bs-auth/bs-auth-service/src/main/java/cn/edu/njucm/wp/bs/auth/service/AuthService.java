package cn.edu.njucm.wp.bs.auth.service;

import cn.edu.njucm.wp.bs.auth.pojo.Permission;
import cn.edu.njucm.wp.bs.auth.pojo.Role;
import cn.edu.njucm.wp.bs.auth.vo.RoleVO;
import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;

import java.util.List;

public interface AuthService {
    Integer create(Role role);

    Integer delete(Integer id);

    List<Role> list();

    Integer update(Role role);

    Boolean check(Role role);

    Role getRoleByUserId(Long id);

    List<Integer> getRoleIdByUserId(Long id);

    String authentication(String username, String password);

    Integer bindRole(Long userId, List<Integer> roleId);

    List<Permission> permissionList();

    List<Integer> getPermissionIdByRoleId(List<Integer> ids);

    Boolean check(Permission permission);

    Boolean create(Permission permission);

    Boolean update(Permission permission);

    PageResult<RoleVO> list(PageParam param);

    Integer bindPermission(Integer roleId, List<Integer> permissionId);

    Integer bindField(Integer id, List<Integer> fieldId);

}
