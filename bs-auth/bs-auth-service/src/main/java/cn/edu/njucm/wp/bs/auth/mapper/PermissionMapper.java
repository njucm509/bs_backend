package cn.edu.njucm.wp.bs.auth.mapper;

import cn.edu.njucm.wp.bs.auth.pojo.Permission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface PermissionMapper extends Mapper<Permission> {
    List<Integer> getPermissionIdByRoleId(@Param("ids") List<Integer> ids);

    List<Integer> getPermissionIdsByRoleId(@Param("id") Integer id);

    Integer check(Permission permission);

    @Insert("insert into role_permission values (#{roleId},#{permissionId},now(),now())")
    Integer bindPermission(@Param("roleId") Integer roleId, @Param("permissionId") Integer id);
}
