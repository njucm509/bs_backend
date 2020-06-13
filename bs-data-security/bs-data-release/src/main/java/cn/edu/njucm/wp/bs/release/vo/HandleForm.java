package cn.edu.njucm.wp.bs.release.vo;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.pojo.UploadDataRecord;
import lombok.Data;

import java.util.List;

@Data
public class HandleForm {
    private Long id;
    private List<Field> fields;
    private Integer ageLevel;
    private Float sim;
    private Character mode;
    private Integer k;
    private Long userId;
    private List<UploadDataRecord> curData;
}
