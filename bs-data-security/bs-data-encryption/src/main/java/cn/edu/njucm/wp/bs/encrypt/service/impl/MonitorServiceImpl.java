package cn.edu.njucm.wp.bs.encrypt.service.impl;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import cn.edu.njucm.wp.bs.encrypt.entity.DeviceInfo;
import cn.edu.njucm.wp.bs.encrypt.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

@Slf4j
@Service
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    private HashMap<String, Connection> connectionMap;

    @Override
    public DeviceInfo getDeviceInfo(String name) throws IOException, InterruptedException {
        log.info("{} monitor", name);
        DeviceInfo deviceInfo = DeviceInfo.getInstance();
        if (connectionMap.containsKey(name)) {
            Connection connection = connectionMap.get(name);
            DeviceInfo.MemoryInfo memInfo = getMemInfo(connection);
            DeviceInfo.CPUInfo cpuInfo = getCpuInfo(connection);
            DeviceInfo.NetInfo netInfo = getNetInfo(connection);
            List<DeviceInfo.DirInfo> dirList = getDirList(connection);
            List<DeviceInfo.IOInfo> ioList = getIoList(connection);
            String macAddress = getMacAddress(connection);
            float onlineTime = getOnlineTime(connection);
            deviceInfo.setCpuInfo(cpuInfo);
            deviceInfo.setDirInfo(dirList);
            deviceInfo.setIoInfo(ioList);
            deviceInfo.setMacAddress(macAddress);
            deviceInfo.setNetInfo(netInfo);
            deviceInfo.setMemoryInfo(memInfo);
            deviceInfo.setOnlineTime(onlineTime);
        }
        log.info("device {}: {}", name, deviceInfo);
        return deviceInfo;
    }

    public ArrayList<String> getStd(Session session, String cmd) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        session.execCommand(cmd);
        InputStream stdout = new StreamGobbler(session.getStdout());

        BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
        String line = null;
        while (true) {
            line = stdoutReader.readLine();
            if (line == null) {
                break;
            }
            lines.add(line);
        }
        session.close();
        return lines;
    }

    public float getOnlineTime(Connection connection) throws IOException {
        ArrayList<String> std = getStd(connection.openSession(), "cat /proc/uptime");
        float onlineTime = 0.0f;
        String line = std.get(0).trim();
        String[] temp = line.split("\\s+");
        onlineTime = Float.parseFloat(temp[0]);
        return onlineTime;
    }

    public String getMacAddress(Connection connection) throws IOException {
        ArrayList<String> std = getStd(connection.openSession(), "ifconfig eno1");
        System.out.println(std);
        String mac = null;
        for (String line : std) {
            if (line.startsWith("ether")) {
                System.out.println(line);
                String[] temp = line.split("\\s+");
                mac = temp[1];
                break;
            }
        }
        return mac;
    }

    public DeviceInfo.MemoryInfo getMemInfo(Connection connection) throws IOException {
        ArrayList<String> lines = getStd(connection.openSession(), "cat /proc/meminfo");
        DeviceInfo.MemoryInfo memoryInfo = DeviceInfo.getInstance().new MemoryInfo();

        for (String line : lines) {
            String[] temp = line.split("\\s+");
            if (temp[0].startsWith("MemTotal:")) {
                // 内存总量
                memoryInfo.setMemTotal(Long.parseLong(temp[1]));
            } else if (temp[0].startsWith("MemFree:")) {
                // 空闲内存量
                memoryInfo.setMemFree(Long.parseLong(temp[1]));
            } else if (temp[0].startsWith("SwapTotal:")) {
                // 交换空间总量
                memoryInfo.setSwapTotal(Long.parseLong(temp[1]));
            } else if (temp[0].startsWith("SwapFree:")) {
                // 空闲交换空间量
                memoryInfo.setSwapFree(Long.parseLong(temp[1]));
            }
        }
        return memoryInfo;
    }

    public DeviceInfo.CPUInfo getCpuInfo(Connection connection) throws IOException {
        ArrayList<String> std = getStd(connection.openSession(), "cat /proc/stat");
        DeviceInfo.CPUInfo cpuInfo = DeviceInfo.getInstance().new CPUInfo();
        StringTokenizer token = new StringTokenizer(std.get(0));
        if (!token.hasMoreTokens()) {
            log.error("没有获取到cpu信息");
            return null;
        }
        token.nextToken();
        cpuInfo.setUser(Integer.parseInt(token.nextToken()));
        cpuInfo.setNice(Integer.parseInt(token.nextToken()));
        cpuInfo.setSystem(Integer.parseInt(token.nextToken()));
        cpuInfo.setIdle(Integer.parseInt(token.nextToken()));

        return cpuInfo;
    }

    public DeviceInfo.NetInfo getNetInfo(Connection connection) throws IOException, InterruptedException {
        ArrayList<String> std1 = getStd(connection.openSession(), "cat /proc/net/dev");

        DeviceInfo.NetInfo netInfo = DeviceInfo.getInstance().new NetInfo();
        String name = "eno1";

        //开始时间 第一次采集
        long startTime = System.currentTimeMillis();

        long receBytes1 = 0, sendBytes1 = 0;
        for (String line : std1) {
            line = line.trim();
            if (line.startsWith(name)) {
                String[] temp = line.split("\\s+");
                //获取 receive字节数
                receBytes1 = Long.parseLong(temp[1]);
                //获取 send字节数
                sendBytes1 = Long.parseLong(temp[9]);
                break;
            }
        }

        Thread.sleep(1000);

        //第二次采集
        long endTime = System.currentTimeMillis();

        ArrayList<String> std2 = getStd(connection.openSession(), "cat /proc/net/dev");

        long receBytes2 = 0, sendBytes2 = 0;
        for (String line : std2) {
            line = line.trim();
            if (line.startsWith(name)) {
                String[] temp = line.split("\\s+");
                //获取 receive字节数
                receBytes2 = Long.parseLong(temp[1]);
                //获取 send字节数
                sendBytes2 = Long.parseLong(temp[9]);
                break;
            }
        }

        netInfo.setReceBytes(receBytes2);
        netInfo.setSendBytes(sendBytes2);
        // 计算上传下载速率
        if (receBytes1 != 0 && receBytes2 != 0 && sendBytes1 != 0 && sendBytes2 != 0) {
            float interval = (float) (endTime - startTime) / 1000;

            float downloadSpeed = (float) ((float) (receBytes2 - receBytes1) / 1024 / interval);
            float uploadSpeed = (float) ((float) (sendBytes2 - sendBytes1) / 1024 / interval);

            // 指定存储格式，字符串
            DecimalFormat df = new DecimalFormat("0.00");
            String dls, uls;

            if ((float) (downloadSpeed / 1024) >= 1) {
                downloadSpeed = (float) (downloadSpeed / 1024);
                dls = df.format(downloadSpeed) + "Mb/s";
            } else {
                dls = df.format(downloadSpeed) + "Kb/s";
            }

            if ((float) (uploadSpeed / 1024) >= 1) {
                uploadSpeed = (float) (uploadSpeed / 1024);
                uls = df.format(uploadSpeed) + "Mb/s";
            } else {
                uls = df.format(uploadSpeed) + "Kb/s";
            }

            netInfo.setCurrentDownloadSpeed(dls);
            netInfo.setCurrentUploadSpeed(uls);

        }
        return netInfo;
    }

    public ArrayList<String> getDirs(Connection connection) throws IOException {
        return getStd(connection.openSession(), "ls /");
    }

    public DeviceInfo.DirInfo getDirInfo(Connection connection, String path) throws IOException {
        DeviceInfo.DirInfo dirInfo = DeviceInfo.getInstance().new DirInfo();
        String cmd = "df -hl /" + path;
        ArrayList<String> std = getStd(connection.openSession(), cmd);
        String line = std.get(1).trim();
        String[] temp = line.split("\\s+");
        dirInfo.setName(path);
//        if (temp[1].endsWith("M")) {
        dirInfo.setTotalSize(temp[1]);
//        }
        dirInfo.setUsedSize(temp[2]);
        dirInfo.setAvailSize(temp[3]);
        return dirInfo;
    }

    public List<DeviceInfo.DirInfo> getDirList(Connection connection) throws IOException {
        ArrayList<String> dirs = getDirs(connection);
        List<DeviceInfo.DirInfo> list = new ArrayList<>();
        for (String dir : dirs) {
            DeviceInfo.DirInfo dirInfo = getDirInfo(connection, dir);
            list.add(dirInfo);
        }
        return list;
    }

    public List<DeviceInfo.IOInfo> getIoList(Connection connection) throws IOException {
        ArrayList<String> std = getStd(connection.openSession(), "iostat -d -x");
        List<DeviceInfo.IOInfo> list = new ArrayList<>();
        int count = 0;
        for (String line : std) {
            if (++count > 3) {
                String[] temp = line.split("\\s+");
                if (temp.length > 1) {
                    DeviceInfo.IOInfo ioInfo = DeviceInfo.getInstance().new IOInfo();
                    ioInfo.setName(temp[0]);
                    ioInfo.setUtil(Float.parseFloat(temp[temp.length - 1]));
                    list.add(ioInfo);
                }
            }
        }
        return list;
    }
}
