package cn.edu.njucm.wp.bs.encrypt.entity;

import java.util.List;

/**
 * 设备基本信息的数据结构
 */

public class DeviceInfo {

    private String macAddress;// mac地址（可以去掉，放在总结构上）
    private MemoryInfo memoryInfo;// 内存信息
    private CPUInfo cpuInfo;// cpu信息
    private NetInfo netInfo;// 网络带宽信息
    private List<IOInfo> ioInfo;// 磁盘IO信息
    private List<DirInfo> dirInfo;// 目录使用情况
    private float onlineTime;// 在线时长

    private static DeviceInfo instance;

    private DeviceInfo() {
    }

    public static DeviceInfo getInstance() {// 单例模式
        if (instance == null) {
            instance = new DeviceInfo();
        }
        return instance;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public MemoryInfo getMemoryInfo() {
        return memoryInfo;
    }

    public void setMemoryInfo(MemoryInfo memoryInfo) {
        this.memoryInfo = memoryInfo;
    }

    public CPUInfo getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(CPUInfo cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public NetInfo getNetInfo() {
        return netInfo;
    }

    public void setNetInfo(NetInfo netInfo) {
        this.netInfo = netInfo;
    }

    public List<IOInfo> getIoInfo() {
        return ioInfo;
    }

    public void setIoInfo(List<IOInfo> ioInfo) {
        this.ioInfo = ioInfo;
    }

    public float getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(float onlineTime) {
        this.onlineTime = onlineTime;
    }

    public List<DirInfo> getDirInfo() {
        return dirInfo;
    }

    public void setDirInfo(List<DirInfo> dirInfo) {
        this.dirInfo = dirInfo;
    }

    public static void setInstance(DeviceInfo instance) {
        DeviceInfo.instance = instance;
    }

    /**
     * memTotal:总容量； memFree：空闲容量 ； swapTotal：交换空间总容量； swapFree：交换空间空闲容量；
     */
    public class MemoryInfo {
        private long memTotal;
        private long memFree;
        private long swapTotal;
        private long swapFree;

        public long getMemTotal() {
            return memTotal;
        }

        public void setMemTotal(long memTotal) {
            this.memTotal = memTotal;
        }

        public long getMemFree() {
            return memFree;
        }

        public void setMemFree(long memFree) {
            this.memFree = memFree;
        }

        public long getSwapTotal() {
            return swapTotal;
        }

        public void setSwapTotal(long swapTotal) {
            this.swapTotal = swapTotal;
        }

        public long getSwapFree() {
            return swapFree;
        }

        public void setSwapFree(long swapFree) {
            this.swapFree = swapFree;
        }

        @Override
        public String toString() {
            return "MemoryInfo{" +
                    "memTotal=" + memTotal +
                    ", memFree=" + memFree +
                    ", swapTotal=" + swapTotal +
                    ", swapFree=" + swapFree +
                    '}';
        }
    }

    /**
     * user:系统启动至今，用户态的CPU时间。1jiffies=0.01s； nice：系统启动至今，nice值为负的进程所占用的CPU时间；
     * system：系统启用至今，核心CPU时间 ； idle：系统启动至今，除IO等待时间以外的其他等待时间； 计算cpu的使用率：
     * 可以使用取两个采样点，计算其差值的办法。 CPU利用率 = 1- (idle2-idle1)/(cpu2-cpu1)
     */
    public class CPUInfo {
        private long user;
        private long nice;
        private long system;
        private long idle;

        public long getUser() {
            return user;
        }

        public void setUser(long user) {
            this.user = user;
        }

        public long getNice() {
            return nice;
        }

        public void setNice(long nice) {
            this.nice = nice;
        }

        public long getSystem() {
            return system;
        }

        public void setSystem(long system) {
            this.system = system;
        }

        public long getIdle() {
            return idle;
        }

        public void setIdle(long idle) {
            this.idle = idle;
        }

        @Override
        public String toString() {
            return "CPUInfo{" +
                    "user=" + user +
                    ", nice=" + nice +
                    ", system=" + system +
                    ", idle=" + idle +
                    '}';
        }
    }

    /**
     * receBytes:接受的字节数，可计算下载带宽 ； sendBytes：发送的字节数，可计算上行带宽；
     * currentDownloadSpeed：当前下载速度 ； currentUploadSpeed：当前上传速度；
     */
    public class NetInfo {
        private long receBytes;
        private long sendBytes;
        private String currentDownloadSpeed;
        private String currentUploadSpeed;

        public long getReceBytes() {
            return receBytes;
        }

        public void setReceBytes(long receBytes) {
            this.receBytes = receBytes;
        }

        public long getSendBytes() {
            return sendBytes;
        }

        public void setSendBytes(long sendBytes) {
            this.sendBytes = sendBytes;
        }

        public String getCurrentDownloadSpeed() {
            return currentDownloadSpeed;
        }

        public void setCurrentDownloadSpeed(String currentDownloadSpeed) {
            this.currentDownloadSpeed = currentDownloadSpeed;
        }

        public String getCurrentUploadSpeed() {
            return currentUploadSpeed;
        }

        public void setCurrentUploadSpeed(String currentUploadSpeed) {
            this.currentUploadSpeed = currentUploadSpeed;
        }

        @Override
        public String toString() {
            return "NetInfo{" +
                    "receBytes=" + receBytes +
                    ", sendBytes=" + sendBytes +
                    ", currentDownloadSpeed='" + currentDownloadSpeed + '\'' +
                    ", currentUploadSpeed='" + currentUploadSpeed + '\'' +
                    '}';
        }
    }

    /**
     * util:一秒中有百分之多少的时间用于I/O操作；
     * 如果%util接近100%,表明I/O请求太多,I/O系统已经满负荷，磁盘可能存在瓶颈,一般%util大于70%,I/O压力就比较大
     */
    public class IOInfo {
        private String name;
        private float util;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getUtil() {
            return util;
        }

        public void setUtil(float util) {
            this.util = util;
        }

        @Override
        public String toString() {
            return "IOInfo{" +
                    "name='" + name + '\'' +
                    ", util=" + util +
                    '}';
        }
    }

    /**
     * name:目录名称  totalSize:目录总容量 ； usedSize：目录已使用大小 ； availSize：目录剩余容量；
     */
    public class DirInfo {
        private String name;
        private String totalSize;
        private String usedSize;
        private String availSize;

        public String getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(String totalSize) {
            this.totalSize = totalSize;
        }

        public String getUsedSize() {
            return usedSize;
        }

        public void setUsedSize(String usedSize) {
            this.usedSize = usedSize;
        }

        public String getAvailSize() {
            return availSize;
        }

        public void setAvailSize(String availSize) {
            this.availSize = availSize;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "DirInfo{" +
                    "name='" + name + '\'' +
                    ", totalSize='" + totalSize + '\'' +
                    ", usedSize='" + usedSize + '\'' +
                    ", availSize='" + availSize + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "macAddress='" + macAddress + '\'' +
                ", memoryInfo=" + memoryInfo +
                ", cpuInfo=" + cpuInfo +
                ", netInfo=" + netInfo +
                ", ioInfo=" + ioInfo +
                ", dirInfo=" + dirInfo +
                ", onlineTime=" + onlineTime +
                '}';
    }
}
