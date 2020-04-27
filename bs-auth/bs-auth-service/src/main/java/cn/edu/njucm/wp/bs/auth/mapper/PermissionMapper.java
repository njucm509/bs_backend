package cn.edu.njucm.wp.bs.auth.mapper;

import cn.edu.njucm.wp.bs.auth.pojo.Permission;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface PermissionMapper extends Mapper<Permission> {
}
