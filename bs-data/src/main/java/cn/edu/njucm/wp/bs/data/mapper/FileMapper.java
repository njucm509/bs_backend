package cn.edu.njucm.wp.bs.data.mapper;

import cn.edu.njucm.wp.bs.data.pojo.File;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface FileMapper extends Mapper<File> {

    @Select("select * from files where user_id = #{id}")
    File getFileByUserId(@Param("id") Long id);
}
