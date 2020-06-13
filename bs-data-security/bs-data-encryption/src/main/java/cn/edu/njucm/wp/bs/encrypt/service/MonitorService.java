package cn.edu.njucm.wp.bs.encrypt.service;


import cn.edu.njucm.wp.bs.encrypt.entity.DeviceInfo;

import java.io.IOException;

public interface MonitorService {
    DeviceInfo getDeviceInfo(String name) throws IOException, InterruptedException;
}
