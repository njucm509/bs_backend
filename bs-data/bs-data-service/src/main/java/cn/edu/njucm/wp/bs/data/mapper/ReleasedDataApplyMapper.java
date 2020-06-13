package cn.edu.njucm.wp.bs.data.mapper;

import cn.edu.njucm.wp.bs.data.pojo.ReleasedDataApply;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface ReleasedDataApplyMapper extends Mapper<ReleasedDataApply>, MySqlMapper<ReleasedDataApply> {
}
