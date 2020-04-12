package cn.edu.njucm.wp.bs.data.bo;

import cn.edu.njucm.wp.bs.data.pojo.MedicalRecord;
import lombok.Data;

@Data
public class UserMedicalRecord extends MedicalRecord {
    private Long userId;
}
