package cn.edu.njucm.wp.bs.data.service.impl;

import cn.edu.njucm.wp.bs.data.mapper.UploadDataRecordMapper;
import cn.edu.njucm.wp.bs.data.pojo.UploadDataRecord;
import cn.edu.njucm.wp.bs.data.service.UploadDataRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UploadDataRecordServiceImpl implements UploadDataRecordService {

    @Autowired
    UploadDataRecordMapper uploadDataRecordMapper;

    @Override
    public List<UploadDataRecord> getRecordByUser(Long id) {
        UploadDataRecord record = new UploadDataRecord();
        record.setUserId(id);
        System.out.println(record);
        List<UploadDataRecord> list = uploadDataRecordMapper.select(record);
        System.out.println(list);
        return list;
    }
}
