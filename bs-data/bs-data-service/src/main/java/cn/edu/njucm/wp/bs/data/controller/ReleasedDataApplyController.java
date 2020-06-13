package cn.edu.njucm.wp.bs.data.controller;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.service.ReleasedDataApplyService;
import cn.edu.njucm.wp.bs.data.vo.ReleasedDataApplyHandleBean;
import cn.edu.njucm.wp.bs.data.vo.ReleasedDataApplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("release/apply")
public class ReleasedDataApplyController {

    @Autowired
    ReleasedDataApplyService releasedDataApplyService;

    @GetMapping("page")
    public ResponseEntity<PageResult<ReleasedDataApplyVO>> getByPage(PageParam param) {
        PageResult<ReleasedDataApplyVO> page = releasedDataApplyService.getByPage(param);

        return ResponseEntity.ok(page);
    }

    @PostMapping("create")
    public ResponseEntity<Boolean> create(@RequestBody ReleasedDataApplyHandleBean releasedDataApplyHandleBean) {
        Boolean res = false;

        System.out.println(releasedDataApplyHandleBean);
        if (releasedDataApplyHandleBean.getApplyUserId().size() == 0) {
            return ResponseEntity.ok(false);
        }

        Boolean flag = releasedDataApplyService.check(releasedDataApplyHandleBean);
        if (!flag) {
            res = releasedDataApplyService.create(releasedDataApplyHandleBean);
        }

        return ResponseEntity.ok(res);
    }

    @GetMapping("check/{id}")
    public ResponseEntity<Boolean> pass(@PathVariable("id") Long id) {
        Boolean flag = releasedDataApplyService.update(id);

        return ResponseEntity.ok(flag);
    }

}
