package cn.edu.njucm.wp.bs.user.mapper;

import cn.edu.njucm.wp.bs.user.pojo.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserMapper extends Mapper<User> {
}
