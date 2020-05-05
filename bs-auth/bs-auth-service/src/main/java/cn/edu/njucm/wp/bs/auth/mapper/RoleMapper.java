package cn.edu.njucm.wp.bs.auth.mapper;

import cn.edu.njucm.wp.bs.auth.pojo.Role;
import cn.edu.njucm.wp.bs.auth.vo.RoleVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RoleMapper extends Mapper<Role> {
    Integer check(Role role);

    Role getRoleByUserId(Long id);

    List<Integer> getRoleIdByUserId(@Param("id") Long id);

    @Insert("insert into user_role values (#{userId},#{roleId},now(),now())")
    Integer bindRole(@Param("userId") Long userId, @Param("roleId") Integer roleId);

    @Delete("delete from role_permission where role_id = #{id}")
    Integer deleteRolePermission(@Param("id") Integer id);

    @Select("select field_id from role_field where role_id = #{id}")
    List<Integer> getFieldIdByRoleId(@Param("id") Integer id);

    @Delete("delete from role_field where role_id = #{id}")
    Integer deleteRoleField(Integer id);

    @Insert("insert into role_field values (#{roleId},#{fieldId},now(),now())")
    Integer bindField(@Param("roleId") Integer roleId, @Param("fieldId") Integer fieldId);

}
