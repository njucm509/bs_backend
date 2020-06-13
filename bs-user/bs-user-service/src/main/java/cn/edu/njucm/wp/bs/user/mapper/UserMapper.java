package cn.edu.njucm.wp.bs.user.mapper;

import cn.edu.njucm.wp.bs.user.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface UserMapper extends Mapper<User>, MySqlMapper<User> {

    @Delete("delete from user_role where user_id = #{id}")
    Integer deleteUserRole(Long id);
}
