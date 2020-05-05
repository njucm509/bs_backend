package cn.edu.njucm.wp.bs.data.mapper;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface FieldMapper extends Mapper<Field> {
    Integer check(Field field);
}
