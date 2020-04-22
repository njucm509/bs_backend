package cn.edu.njucm.wp.bs.auth.mapper;

import cn.edu.njucm.wp.bs.auth.pojo.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AuthMapper extends Mapper<Role> {
    Integer check(Role role);

    Role getRoleByUserId(Long id);

    List<Integer> getRoleIdByUserId(@Param("id") Long id);

    @Insert("insert into user_role values (#{userId},#{roleId},now(),now())")
    Integer bindRole(@Param("userId") Long userId, @Param("roleId") Integer roleId);
}
