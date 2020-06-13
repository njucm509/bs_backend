package cn.edu.njucm.wp.bs.data.service;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.pojo.Type;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FieldService {
    Boolean create(Field field);

    Boolean check(Field field);

    List<Field> list();

    PageResult<Field> getFieldByPageAndSort(PageParam param);

    List<Field> getFieldByUserId(Long id);

    Boolean init(String tableName, int i);

    Boolean checkTable(String tableName);

    Boolean updateTableScheme();

    List<Type> getTypeList();

    Boolean update(Field field);

    List<Field> getAnomyousFieldByUserId(Long id, String date);

    void dropInitTable(List<String> list);

    List<Field> getFieldByIds(String ids);
}
