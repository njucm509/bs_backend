package cn.edu.njucm.wp.bs.data.mapper;

import cn.edu.njucm.wp.bs.data.pojo.ReleasedRecord;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface ReleasedRecordMapper extends Mapper<ReleasedRecord>, MySqlMapper<ReleasedRecord> {
}
