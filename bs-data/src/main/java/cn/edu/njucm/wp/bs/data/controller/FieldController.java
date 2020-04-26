package cn.edu.njucm.wp.bs.data.controller;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("field")
public class FieldController {

    @PostMapping("create")
    public ResponseEntity<Boolean> create(Field field) {

    }
}
