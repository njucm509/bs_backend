package cn.edu.njucm.wp.bs.data.vo;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.pojo.UploadDataRecord;
import lombok.Data;

import java.util.List;

@Data
public class FileParam {
    private Long id;
    private int type = 0;
    private List<UploadDataRecord> date;
    private List<Field> fields = null;
}
