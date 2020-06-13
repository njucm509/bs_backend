package cn.edu.njucm.wp.bs.data.vo;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.pojo.File;
import lombok.Data;

import java.util.List;

@Data
public class FileField {
    File file;

    List<Field> fields;
}
