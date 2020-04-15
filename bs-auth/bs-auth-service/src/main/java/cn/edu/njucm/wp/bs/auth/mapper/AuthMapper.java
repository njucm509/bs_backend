package cn.edu.njucm.wp.bs.auth.mapper;

import cn.edu.njucm.wp.bs.auth.pojo.Role;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface AuthMapper extends Mapper<Role> {
    Integer check(Role role);
}
