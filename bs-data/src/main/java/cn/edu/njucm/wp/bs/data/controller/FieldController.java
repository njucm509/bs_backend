package cn.edu.njucm.wp.bs.data.controller;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("field")
public class FieldController {

    @Autowired
    FieldService fieldService;

    @PostMapping("create")
    public ResponseEntity<Boolean> create(Field field) {
        Boolean flag = fieldService.check(field);
        if (flag) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(fieldService.create(field));
    }

    private Boolean check(Field field) {

        return fieldService.check(field);
    }

    @GetMapping("page")
    public ResponseEntity<PageResult<Field>> list(PageParam param) {
        PageResult<Field> result = fieldService.getFieldByPageAndSort(param);
        if (result == null | result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("list")
    public ResponseEntity<List<Field>> list() {
        List<Field> list = fieldService.list();
        if (CollectionUtils.isEmpty(list)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
}
