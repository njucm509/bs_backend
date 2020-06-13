package cn.edu.njucm.wp.bs.encrypt.controller;

import cn.edu.njucm.wp.bs.encrypt.entity.EnvInfo;
import cn.edu.njucm.wp.bs.encrypt.service.EnvInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

@RestController
@RequestMapping("/api")
public class EnvInfoController {

    @Autowired
    EnvInfoService service;

    @RequestMapping("/device/env")
    public EnvInfo init() {
        EnvInfo envInfo = null;
        try {
            envInfo = service.init();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return envInfo;
    }
}
