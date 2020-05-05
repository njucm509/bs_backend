package cn.edu.njucm.wp.bs.data.service;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.pojo.Field;

import java.util.List;

public interface FieldService {
    Boolean create(Field field);

    Boolean check(Field field);

    List<Field> list();

    PageResult<Field> getFieldByPageAndSort(PageParam param);
}
