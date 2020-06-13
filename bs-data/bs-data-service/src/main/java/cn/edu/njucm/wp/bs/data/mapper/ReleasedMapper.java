package cn.edu.njucm.wp.bs.data.mapper;

import cn.edu.njucm.wp.bs.data.pojo.Released;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;

@Repository
public interface ReleasedMapper extends Mapper<Released> {
    HashMap<String, Object> check(Released released);
}
