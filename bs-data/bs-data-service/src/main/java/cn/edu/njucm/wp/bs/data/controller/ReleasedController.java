package cn.edu.njucm.wp.bs.data.controller;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.pojo.Released;
import cn.edu.njucm.wp.bs.data.service.ReleasedService;
import cn.edu.njucm.wp.bs.data.vo.ReleasedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("released")
public class ReleasedController {

    @Autowired
    ReleasedService releasedService;

    @GetMapping("page")
    public ResponseEntity<PageResult<ReleasedVO>> getReleasedByPage(PageParam param) {
        PageResult<ReleasedVO> result = releasedService.getReleasedByPage(param);
        if (result == null | result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("list")
    public ResponseEntity<List<ReleasedVO>> getReleased() {
        List<ReleasedVO> list = releasedService.getReleased();
        if (CollectionUtils.isEmpty(list)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(list);
    }

    @PostMapping("create")
    public ResponseEntity<Integer> create(@RequestBody Released released) {
        System.out.println(released);
        Boolean flag = releasedService.check(released);
        if (flag) {
            return ResponseEntity.ok(-1);
        }
        Integer id = releasedService.create(released);

        return ResponseEntity.ok(id);
    }

}
