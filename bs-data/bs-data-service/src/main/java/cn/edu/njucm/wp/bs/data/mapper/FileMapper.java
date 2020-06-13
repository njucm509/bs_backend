package cn.edu.njucm.wp.bs.data.mapper;

import cn.edu.njucm.wp.bs.data.pojo.File;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface FileMapper extends Mapper<File> {

    @Select("select * from files where user_id = #{id}")
    File getFileByUserId(@Param("id") Long id);

    Integer insert2init(@Param("cols") String cols, @Param("items") List<String> items);

    List<Map<String, Object>> getDataByUserId(@Param("cols") List<String> cols, @Param("id") Long id);

    Integer insert2db(@Param("cols") String cols, @Param("items") List<String> items);

    List<Map<String, Object>> getAnonymousDataByUserId(@Param("cols") List<String> cols, @Param("id") Long id);

    List<Map<String, Object>> getDataByUserIdAndDate(@Param("cols") List<String> cols, @Param("id") Long id, @Param("date") List<String> date);

    List<Map<String, Object>> getAnonymousDataByUserIdAndDate(@Param("cols") List<String> cols, @Param("id") Long id, @Param("date") List<String> date);

    @Select("select id from ${table} where user_id = #{userId} and date = #{date}")
    List<Long> getIdsByDateAndUserId(String table, String date, Long userId);

    List<Map<String, Object>> getDataByIdAndDate(String table, Long id, List<String> date);
}
