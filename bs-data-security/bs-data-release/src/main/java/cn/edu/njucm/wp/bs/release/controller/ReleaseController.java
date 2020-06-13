package cn.edu.njucm.wp.bs.release.controller;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.release.pojo.HandleFormBean;
import cn.edu.njucm.wp.bs.release.service.HandleFormBeanService;
import cn.edu.njucm.wp.bs.release.service.ReleaseService;
import cn.edu.njucm.wp.bs.release.vo.HandleForm;
import cn.edu.njucm.wp.bs.release.vo.HandleFormBeanVO;
import cn.edu.njucm.wp.bs.release.vo.ReleaseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class ReleaseController {

    @Autowired
    ReleaseService releaseService;

    @Autowired
    HandleFormBeanService handleFormBeanService;

    @PostMapping("anonymous")
    public ResponseEntity<HashMap<String, Object>> handle(@RequestBody HandleForm form) {
        HashMap<String, Object> msg = new HashMap<>();
        List<Long> list = form.getFields().stream().map(Field::getId).distinct().collect(Collectors.toList());
        StringBuilder fields = new StringBuilder();
        for (Long id : list) {
            fields.append(id);
            fields.append(",");
        }

        HandleFormBean handleFormBean = new HandleFormBean();
        BeanUtils.copyProperties(form, handleFormBean);
        handleFormBean.setUsedFields(fields.toString());
        System.out.println(handleFormBean);
        Long id = handleFormBeanService.create(handleFormBean);

        System.out.println(id);

        if (form.getMode() == 'k') {
            msg = releaseService.kAnonymous(form);
            if (msg != null) {
                handleFormBeanService.update(id);
            }
        }

        msg.put("handleFormId", id);

        System.out.println(msg);
        return ResponseEntity.ok(msg);
    }

    @PostMapping("spark/anonymous")
    public ResponseEntity<String> handleBySpark(HandleForm form) {
        String msg = null;
        if (form.getMode() == 'k') {
            msg = releaseService.kAnonymousBySpark(form);
        }

        return ResponseEntity.ok(msg);
    }

    @PostMapping("all")
    public ResponseEntity<Integer> handleRelease(@RequestBody ReleaseBean bean) {

        System.out.println(bean);

        return ResponseEntity.ok(releaseService.handleRelease(bean));
    }

    @GetMapping("handleForm/{id}")
    public ResponseEntity<HandleFormBeanVO> getHandleFormById(@PathVariable("id") Long id) {
        HandleFormBeanVO bean = handleFormBeanService.getHandleFormById(id);

        return ResponseEntity.ok(bean);
    }
}
