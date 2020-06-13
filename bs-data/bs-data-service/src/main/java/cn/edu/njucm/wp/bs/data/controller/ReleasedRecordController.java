package cn.edu.njucm.wp.bs.data.controller;

import cn.edu.njucm.wp.bs.data.pojo.ReleasedRecord;
import cn.edu.njucm.wp.bs.data.service.ReleasedRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("record/releaseCheck")
public class ReleasedRecordController {

    @Autowired
    ReleasedRecordService releasedRecordService;
    @PostMapping("create")
    public ResponseEntity<Boolean> create(ReleasedRecord releasedRecord) {
        Boolean res = releasedRecordService.create(releasedRecord);

        return ResponseEntity.ok(res);
    }
}
