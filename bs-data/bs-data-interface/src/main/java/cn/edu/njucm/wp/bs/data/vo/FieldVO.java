package cn.edu.njucm.wp.bs.data.vo;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import lombok.Data;

@Data
public class FieldVO extends Field {

    // 0 不可读 1 可读 2 可写
    private Integer auth = 1;
}
