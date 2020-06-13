package cn.edu.njucm.wp.bs.data.service.impl;

import cn.edu.njucm.wp.bs.data.mapper.ReleasedRecordMapper;
import cn.edu.njucm.wp.bs.data.pojo.ReleasedRecord;
import cn.edu.njucm.wp.bs.data.service.ReleasedRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ReleasedRecordServiceImpl implements ReleasedRecordService {

    @Autowired
    ReleasedRecordMapper releasedRecordMapper;

    @Override
    public Boolean create(ReleasedRecord releasedRecord) {
        releasedRecord.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        releasedRecord.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));

        return releasedRecordMapper.insertSelective(releasedRecord) == 1;
    }
}
