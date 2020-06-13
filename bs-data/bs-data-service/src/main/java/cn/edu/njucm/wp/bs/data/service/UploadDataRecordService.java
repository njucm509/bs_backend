package cn.edu.njucm.wp.bs.data.service;

import cn.edu.njucm.wp.bs.data.pojo.UploadDataRecord;

import java.util.List;

public interface UploadDataRecordService {
    List<UploadDataRecord> getRecordByUser(Long id);
}
