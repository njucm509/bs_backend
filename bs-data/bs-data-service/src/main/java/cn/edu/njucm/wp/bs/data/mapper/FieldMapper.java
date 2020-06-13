package cn.edu.njucm.wp.bs.data.mapper;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface FieldMapper extends Mapper<Field> {
    Map<String, Object> check(Field field);

    List<Integer> getFieldIdByRoleIds(@Param("roleIds") List<Integer> roleIds);

    List<Field> getFieldByIds(@Param("ids") List<Integer> ids);

    Integer createTable(@Param("table") String tableName, @Param("cols") List<String> cols);

    @Select("select count(*) from information_schema.TABLES where table_name = #{tableName}")
    Integer isExist(@Param("tableName") String tableName);

    Integer updateTableScheme();

    void drop(@Param("table") String name);

}
