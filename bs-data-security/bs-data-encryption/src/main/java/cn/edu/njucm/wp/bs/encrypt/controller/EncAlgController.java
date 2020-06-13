package cn.edu.njucm.wp.bs.encrypt.controller;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.pojo.EncAlg;
import cn.edu.njucm.wp.bs.encrypt.service.EncAlgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/alg")
public class EncAlgController {

    @Autowired
    private EncAlgService service;

    @GetMapping("page")
    public ResponseEntity<PageResult<EncAlg>> queryEncRecordByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                   @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                                                   @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                   @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                                   @RequestParam(value = "key", required = false) String key) {
        PageParam pageParam = new PageParam(page, rows, sortBy, desc, key);
        log.info("pageParam: {}", pageParam);
        PageResult<EncAlg> result = service.queryUserByPageAndSort(pageParam);
        if (result == null || result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }


    @GetMapping("list")
    public ResponseEntity<List<EncAlg>> getEncAlgList() {
        List<EncAlg> list = service.getEncAlgList();
        if (CollectionUtils.isEmpty(list)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(list);
    }

    @RequestMapping("update")
    public void updateStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
        log.info("update status: {}---{}", id, status);
        EncAlg encAlg = new EncAlg();
        encAlg.setId(id);
        encAlg.setStatus(status);
//        encAlg.setUpdatedAt(new Date());
        service.updateStatus(encAlg);
    }
}
