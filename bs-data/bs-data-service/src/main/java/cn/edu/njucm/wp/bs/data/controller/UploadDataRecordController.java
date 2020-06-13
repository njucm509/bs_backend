package cn.edu.njucm.wp.bs.data.controller;

import cn.edu.njucm.wp.bs.data.pojo.UploadDataRecord;
import cn.edu.njucm.wp.bs.data.service.UploadDataRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("record/upload")
public class UploadDataRecordController {

    @Autowired
    UploadDataRecordService uploadDataRecordService;

    @RequestMapping("user/{id}")
    public ResponseEntity<List<UploadDataRecord>> getRecordByUser(@PathVariable("id") Long id) {
        System.out.println(id);
        List<UploadDataRecord> result = uploadDataRecordService.getRecordByUser(id);
        System.out.println(result);
        return ResponseEntity.ok(result);
    }
}
