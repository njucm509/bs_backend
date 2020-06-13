package cn.edu.njucm.wp.bs.encrypt.controller;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.encrypt.pojo.EncRecord;
import cn.edu.njucm.wp.bs.encrypt.service.EncRecordService;
import cn.edu.njucm.wp.bs.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class EncRecordController {

    @Autowired
    private EncRecordService service;

    @RequestMapping("/encrecord/page")
    public ResponseEntity<PageResult<EncRecord>> queryEncRecordByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                      @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                                                      @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                      @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                                      @RequestParam(value = "key", required = false) String key) {
        PageParam pageParam = new PageParam(page, rows, sortBy, desc, key);
        log.info("pageParam: {}", pageParam);
        PageResult<EncRecord> result = service.queryUserByPageAndSort(pageParam);
        if (result == null || result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/encrecord/user/{id}")
    public ResponseEntity<PageResult<EncRecord>> queryEncRecordByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                      @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                                                      @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                      @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                                      @RequestParam(value = "key", required = false) String key,
                                                                      @PathVariable("id") Long id,
                                                                      @RequestParam(value = "role") Integer role) {
        PageParam pageParam = new PageParam(page, rows, sortBy, desc, key);
        User user = new User();
        user.setId(id);
//        user.setRole(role);
        log.info("pageParam: {} -- user role: {} -- id: {}", pageParam, role, id);
        PageResult<EncRecord> result = service.queryUserByPageAndSort(pageParam, user);
        if (result == null || result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/encrecord/create")
    public void create(@RequestBody EncRecord encRecord) {
        log.info("EncRecord: {}", encRecord);
        service.create(encRecord);

    }

    @RequestMapping("/encrecord/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        log.info("EncRecord_id: {}", id);
        EncRecord encRecord = new EncRecord();
        encRecord.setId(id);
        service.delete(encRecord);
    }
}
