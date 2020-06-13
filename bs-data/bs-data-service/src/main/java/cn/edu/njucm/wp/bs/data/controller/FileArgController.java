package cn.edu.njucm.wp.bs.data.controller;

import cn.edu.njucm.wp.bs.data.service.FileArgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class FileArgController {

    @Autowired
    FileArgService service;

    @RequestMapping("/file/arg/{id}")
    public ResponseEntity<HashMap<String, String>> getFileArg(@PathVariable("id") Integer id) {
        HashMap<String, String> res = service.getFileArg(id);
        if (CollectionUtils.isEmpty(res)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(res);
        }
    }

}
