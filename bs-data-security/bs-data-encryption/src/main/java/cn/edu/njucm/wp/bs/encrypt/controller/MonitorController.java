package cn.edu.njucm.wp.bs.encrypt.controller;

import cn.edu.njucm.wp.bs.encrypt.entity.DeviceInfo;
import cn.edu.njucm.wp.bs.encrypt.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class MonitorController {

    @Autowired
    MonitorService monitorService;

    @RequestMapping("/device/{name}")
    public DeviceInfo info(@PathVariable(value = "name") String name) {
        log.info("{} start monitor...", name);
        DeviceInfo deviceInfo = null;
        try {
            deviceInfo = monitorService.getDeviceInfo(name);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return deviceInfo;
    }

    @RequestMapping("/device/init/{name}")
    public List<DeviceInfo> init(@PathVariable(value = "name") String name) {
        List<DeviceInfo> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            try {
                list.add(monitorService.getDeviceInfo(name));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @RequestMapping("/test")
    public void test() {
    }
}
